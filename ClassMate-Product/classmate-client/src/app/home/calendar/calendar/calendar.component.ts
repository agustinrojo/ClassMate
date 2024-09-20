import { AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { CalendarApi, CalendarOptions, DateSelectArg, EventClickArg, EventDropArg, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg, EventResizeDoneArg } from '@fullcalendar/interaction';
import bootstrapPlugin from '@fullcalendar/bootstrap'
import esLocale from '@fullcalendar/core/locales/es';
import { FullCalendarComponent } from '@fullcalendar/angular';
import { MatDialog } from '@angular/material/dialog';
import { CalendarService } from '../../../services/calendar.service';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { EventImpl } from '@fullcalendar/core/internal';
import { DetailDialogComponent } from '../detail-dialog/detail-dialog.component';
import { OAuth2Service } from '../../../services/oauth2-service';
import { EventResponseDTO } from '../../../services/dto/calendar/event-response-dto.interface';
import { EventDialogComponent } from '../event-dialog/event-dialog.component';
import { EventData } from '../../interfaces/calendar/event-dialog/event-data.interface';
import { EventDataResult } from '../../interfaces/calendar/event-dialog/event-data-result.interface';
import { EventRequestDTO } from '../../../services/dto/calendar/event-request-dto.interface';
import { EventUpdateDTO } from '../../../services/dto/calendar/event-update-dto.interface';
import { User } from '../../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CalendarComponent implements OnInit, AfterViewInit {
  @ViewChild('calendar', { static: false }) fullCalendar!: FullCalendarComponent;
  private calendarApi!: CalendarApi;
  user!: User;
  loggedUserId!: number;
  calendarOptions?: CalendarOptions;
  selectedEvent: any = { id: '', title: '', start: '', end: '' }; // Event object to bind with modal inputs
  eventsArray: EventInput[] = [];

  constructor(
    public dialog: MatDialog,
    private _calendarService: CalendarService,
    private _authService: AuthServiceService,
    private _oauth2Service: OAuth2Service
  ){
    this.loggedUserId = this._authService.getUserId();
    this.user = this._authService.getUser();
  }

  ngOnInit(): void {

    this.loadEvents();

    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [
        dayGridPlugin,
        interactionPlugin,
        bootstrapPlugin
      ],
      headerToolbar: {
        left: 'prev, next today',
        center: 'title',
        right: ''
      },
      themeSystem: 'bootstrap',
      locales: [esLocale],
      locale: 'es',
      height: 'auto',
      events: this.eventsArray,
      editable: true,
      selectable: true,
      //dateClick: (arg) => this.handleDateClick(arg),
      select: this.handleDateSelect.bind(this),
      eventClick: this.handleEventClick.bind(this),
      eventDrop: this.handleEventDrop.bind(this), // Handle drag/drop eventEntities
      eventResize: this.handleEventResize.bind(this),
      progressiveEventRendering: true,
      dayMaxEvents: 3
    }

  }

  ngAfterViewInit(): void {
    this.calendarApi = this.fullCalendar.getApi();
  }

  private loadEvents() {
    this._calendarService.getEventsByUserId().subscribe((resp: EventResponseDTO[]) => {
      console.log('Cuando los trae', resp)
      this.eventsArray = this.mapEventResponseDTOToEventInput(resp);
    });
  }
// as
  private mapEventResponseDTOToEventInput( eventList:EventResponseDTO[] ):EventInput[]  {
    let eventEntities: EventInput[] = [];
    eventList.forEach((e:EventResponseDTO) => {
      let eventInput: EventInput = {
        id: e.id.toString(),
        title: e.title,
        extendedProps: {
          description: e.description
        },
        start: e.startDate,
        end: e.endDate
      }
      eventEntities.push(eventInput);
    });
    return eventEntities;
  }


  private handleDateSelect(selectInfo: DateSelectArg) {
    if (selectInfo.view.type !== 'dayGridMonth') {
      return; // Solo actuar si no es un clic en 'dayGridMonth'
    }
    console.log(selectInfo);// // Open modal
    this.openEventDialogAfterSelect(selectInfo);
  }

  private handleDateClick(clickInfo: DateClickArg){
    console.log(clickInfo);
    this.openEventDialogAfterClick(clickInfo);
  }

  private openEventDialogAfterClick(clickInfo: DateClickArg){
    const dialogRef = this.dialog.open(EventDialogComponent, {
      width: '250px',
      height: '500px',
      data: {
        title: "",
        description: '',
        start: clickInfo.dateStr,
        end: clickInfo.dateStr,
      }
    });

    dialogRef.afterClosed().subscribe((data: EventData) => {
      this.closeDialogEvent(data);
    });
  }

  private openEventDialogAfterSelect(selectInfo: DateSelectArg){
    const dialogRef = this.dialog.open(EventDialogComponent, {
      width: '250px',
      height: '500px',
      data: {
        title: "",
        description: '',
        start: selectInfo.startStr,
        end: selectInfo.endStr,
      }
    });

    dialogRef.afterClosed().subscribe((data: EventData) => {
      this.closeDialogEvent(data);
    });
  }

  private handleEventClick(eventInfo: EventClickArg) {
    let eventEntity = eventInfo.event;

    console.log(eventEntity);
    // // Open modal
    // this.openEventDialogAfterEventClick(eventEntity);
    this.openDetailDialog(eventEntity)
  }

  private handleEventDrop(eventInfo: EventDropArg) {
    const { event } = eventInfo;
    const result: EventDataResult = {
      eventId: Number(event.id),
      title: event.title,
      start: event.startStr,
      end: event.endStr
    };

    this.updateEvent(result.eventId!, result);
  }

  private handleEventResize(eventInfo: EventResizeDoneArg) {
    const { event } = eventInfo;
    const result: EventDataResult = {
      eventId: Number(event.id),
      title: event.title,
      start: event.start!.toISOString(),
      end: event.end!.toISOString()
    };

    this.updateEvent(result.eventId!, result);
  }


  private openEventDialogAfterEventClick(eventInfo: EventImpl){
    const dialogRef = this.dialog.open(EventDialogComponent, {
      width: '250px',
      height: '500px',

      data: {
        id: eventInfo.id,
        title: eventInfo.title,
        description: eventInfo.extendedProps['description'],
        start: eventInfo.startStr,
        end: eventInfo.endStr,
      }
    });

    dialogRef.afterClosed().subscribe((result: EventDataResult) => {
      if (!result) {
        // No action was taken; simply close the dialog
        return;
      }

      if (result.delete) {
        // Handle eventEntity deletion
        this.deleteEvent(result.eventId!);
      } else if (result.title && (result.start || result.end)) {
        // Handle eventEntity update
        this.updateEvent(Number(eventInfo.id), result);
      }
      // If neither delete nor update, no action is required.
    });
  }



  private closeDialogEvent(data: EventData){
    this.saveEvent(data);
  }

  private openDetailDialog(eventInfo: EventImpl){
    console.log(eventInfo);
    const dialogRef = this.dialog.open(DetailDialogComponent, {
      data: {
        id: eventInfo.id,
        title: eventInfo.title,
        description: eventInfo.extendedProps['description'],
        start: eventInfo.start,
        end: eventInfo.end
      }
    })
    dialogRef.afterClosed().subscribe((result:EventDataResult) => {
      // Aquí puedes manejar lo que suceda después de cerrar el diálogo
      if(!result){
        return;
      } else if(result.edit) {
        this.openEditEventDialog(eventInfo);
      } else if(result.delete) {
        this.deleteEvent(result.eventId!)
      }
    });
  }

  private openEditEventDialog(eventInfo: EventImpl){
    const dialogRef = this.dialog.open(EventDialogComponent, {
      width: '250px',
      height: '500px',
      data: {
        id: eventInfo.id,
        title: eventInfo.title,
        description: eventInfo.extendedProps['description'],
        start: eventInfo.startStr,
        end: eventInfo.endStr,
      }
    });

    dialogRef.afterClosed().subscribe((result: EventDataResult) => {
      if (!result) {
        // No action was taken; simply close the dialog
        return;
      }
      if (result.delete) {
        // Handle eventEntity deletion
        this.deleteEvent(result.eventId!);
      } else if (result.title && (result.start || result.end)) {
        // Handle eventEntity update
        this.updateEvent(Number(eventInfo.id), result);
      }
      // If neither delete nor update, no action is required.
    })
  }

  private saveEvent( data: EventData ): void {
      // Check if the title is valid
  if (!data.title || data.title.trim() === "" || data.title.length > 30) {
    return; // Exit early if title is not valid
  }

    let eventEntity: EventRequestDTO = {
      userId: this.loggedUserId,
      title: data.title,
      description: data.description,
      startDate: data.start,
      endDate: data.end
    }
    this._calendarService.saveEvent(eventEntity).subscribe((resp: EventResponseDTO) => {
      console.log('Event saved: ', resp);


      // Add a new eventEntity to the calendar using the backend-generated ID
      this.calendarApi.addEvent({
        id: resp.id.toString(),
        title: resp.title,
        extendedProps: {
          description: resp.description
        },
        start: resp.startDate,
        end: resp.endDate,
        allDay: true
    });

    }, (err) => {
      console.log(err);
    })
  }

  updateEvent(id: number, result: EventDataResult) {
      // Check if the title is valid
  if (!result.title || result.title.trim() === "" || result.title.length > 30) {
    console.warn('Invalid title. Please ensure the title is not empty and has less than 30 characters.');
    return; // Exit early if title is not valid
  }

    // Convert start and end dates from string to Date
    const startDate = new Date(result.start!);
    const endDate = result.end ? new Date(result.end) : null;

    console.log("end", endDate)

    let eventUpdateDTO: EventUpdateDTO = {
      title: result.title!,
      description: result.description!,
      startDate: startDate,
      endDate: endDate!
    }
    this._calendarService.updateEvent(id, eventUpdateDTO).subscribe(() => {
      console.log(`Event with ID ${id} updated successfully`);
      // Update eventEntity in Calendar View
      const eventEntity = this.calendarApi.getEventById(id.toString());

      if (eventEntity) {
        eventEntity.setProp('title', result.title); // Update title
        eventEntity.setExtendedProp('description', result.description);
        eventEntity.setStart(result.start!); // Update start date
        eventEntity.setEnd(result.end!); // Update end date
      } else {
        console.warn(`Event with ID ${id} not found in calendar`);
      }
    }, (error) => {
      console.error('Error updating eventEntity:', error);
    });
  }

  public connectToGoogle(){
    this._oauth2Service.connectToGoogle();
    this.user.synced = true;
    this._authService.setSynced(true);
  }

  public unsyncronize(){
    this._oauth2Service.unsycronize().subscribe((resp) => {
      console.log(resp);
      this.user.synced = false;
      this._authService.setSynced(false);
    })

  }

  private deleteEvent(eventId: number): void {
    this._calendarService.deleteEvent(eventId).subscribe(() => {
      console.log(`Event with ID ${eventId} deleted successfully`);
      this.removeEventFromCalendar(eventId);
    }, (error) => {
      console.error('Error deleting eventEntity:', error);
    });
  }

  private removeEventFromCalendar(eventId: number): void {
    // Find the eventEntity by ID
    const eventEntity = this.calendarApi.getEventById(eventId.toString());

    if (eventEntity) {
        eventEntity.remove(); // Remove the eventEntity from the calendar view
    } else {
        console.warn(`Event with ID ${eventId} not found in calendar`);
    }
}

}
