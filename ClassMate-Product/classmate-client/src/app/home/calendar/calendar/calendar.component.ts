import { AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { CalendarApi, CalendarOptions, DateSelectArg, EventClickArg, EventDropArg, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg, EventResizeDoneArg } from '@fullcalendar/interaction';
import bootstrapPlugin from '@fullcalendar/bootstrap'
import esLocale from '@fullcalendar/core/locales/es';
import { FullCalendarComponent } from '@fullcalendar/angular';
import { MatDialog } from '@angular/material/dialog';
import { EventDialogComponent } from '../event-dialog/event-dialog.component';
import { EventData } from '../../interfaces/calendar/event-dialog/event-data.interface';
import { CalendarService } from '../../../services/calendar.service';
import { EventResponseDTO } from '../../../services/dto/calendar/event-response-dto.interface';
import { EventRequestDTO } from '../../../services/dto/calendar/event-request-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { EventImpl } from '@fullcalendar/core/internal';
import { EventUpdateDTO } from '../../../services/dto/calendar/event-update-dto.interface';
import { EventDataResult } from '../../interfaces/calendar/event-dialog/event-data-result.interface';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CalendarComponent implements OnInit, AfterViewInit {
  @ViewChild('calendar', { static: false }) fullCalendar!: FullCalendarComponent;
  private calendarApi!: CalendarApi;
  loggedUserId!: number;
  calendarOptions?: CalendarOptions;
  selectedEvent: any = { id: '', title: '', start: '', end: '' }; // Event object to bind with modal inputs
  eventsArray: EventInput[] = [];

  constructor(
    public dialog: MatDialog,
    private _calendarService: CalendarService,
    private _authService: AuthServiceService
  ){
    this.loggedUserId = _authService.getUserId();
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
      select: this.handleDateSelect.bind(this),
      eventClick: this.handleEventClick.bind(this),
      eventDrop: this.handleEventDrop.bind(this), // Handle drag/drop events
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
    let events: EventInput[] = [];
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
      events.push(eventInput);
    });
    return events;
  }


  private handleDateSelect(selectInfo: DateSelectArg) {
    if (selectInfo.view.type !== 'dayGridMonth') {
      return; // Solo actuar si no es un clic en 'dayGridMonth'
    }
    // // Open modal
    this.openEventDialogAfterSelect(selectInfo);
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
    let event = eventInfo.event;
    // // Open modal
    this.openEventDialogAfterEventClick(event);
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
        // Handle event deletion
        this.deleteEvent(result.eventId!);
      } else if (result.title && (result.start || result.end)) {
        // Handle event update
        this.updateEvent(Number(eventInfo.id), result);
      }
      // If neither delete nor update, no action is required.
    });
  }



  private closeDialogEvent(data: EventData){
    this.saveEvent(data);
  }

  private saveEvent( data: EventData ): void {
      // Check if the title is valid
  if (!data.title || data.title.trim() === "" || data.title.length > 30) {
    return; // Exit early if title is not valid
  }

    let event: EventRequestDTO = {
      userId: this.loggedUserId,
      title: data.title,
      description: data.description,
      startDate: data.start,
      endDate: data.end
    }
    this._calendarService.saveEvent(event).subscribe((resp: EventResponseDTO) => {
      console.log('Event saved: ', resp);


      // Add a new event to the calendar using the backend-generated ID
      this.calendarApi.addEvent({
        id: resp.id.toString(),
        title: resp.title,
        extendedProps: {
          description: resp.description
        },
        start: resp.startDate,
        end: resp.endDate
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

    let eventUpdateDTO: EventUpdateDTO = {
      title: result.title!,
      description: result.description!,
      startDate: startDate,
      endDate: endDate!
    }
    this._calendarService.updateEvent(id, eventUpdateDTO).subscribe(() => {
      console.log(`Event with ID ${id} updated successfully`);
      // Update event in Calendar View
      const event = this.calendarApi.getEventById(id.toString());

      if (event) {
        event.setProp('title', result.title); // Update title
        event.setStart(result.start!); // Update start date
        event.setEnd(result.end!); // Update end date
      } else {
        console.warn(`Event with ID ${id} not found in calendar`);
      }
    }, (error) => {
      console.error('Error updating event:', error);
    });
  }

  private deleteEvent(eventId: number): void {
    this._calendarService.deleteEvent(eventId).subscribe(() => {
      console.log(`Event with ID ${eventId} deleted successfully`);
      this.removeEventFromCalendar(eventId);
    }, (error) => {
      console.error('Error deleting event:', error);
    });
  }

  private removeEventFromCalendar(eventId: number): void {
    // Find the event by ID
    const event = this.calendarApi.getEventById(eventId.toString());

    if (event) {
        event.remove(); // Remove the event from the calendar view
    } else {
        console.warn(`Event with ID ${eventId} not found in calendar`);
    }
}

}
