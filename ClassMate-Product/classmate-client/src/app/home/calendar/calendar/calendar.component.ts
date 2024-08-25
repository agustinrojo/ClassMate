import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { CalendarOptions, DateSelectArg, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import bootstrapPlugin from '@fullcalendar/bootstrap'
import esLocale from '@fullcalendar/core/locales/es';
import { FullCalendarComponent } from '@fullcalendar/angular';
import { MatDialog } from '@angular/material/dialog';
import { EventDialogComponent } from '../event-dialog/event-dialog.component';
import { EventData } from '../../interfaces/calendar/event-dialog/event-data.interface';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CalendarComponent implements OnInit {
  @ViewChild('calendar', { static: false }) fullCalendar!: FullCalendarComponent;
  calendarOptions?: CalendarOptions;
  selectedEvent: any = { id: '', title: '', start: '', end: '' }; // Event object to bind with modal inputs
  eventsArray: EventInput[] = [ // Explicitly define events as an array
    {
      id: 'a',
      title: 'Petardo',
      start: '2024-08-25',
      end: '2024-08-27'
    }
  ];

  constructor(
    public dialog: MatDialog
  ){}


  ngOnInit(): void {

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
      // events: '/api/events', // TODO: Traer eventos del back
      events: this.eventsArray,
      editable: true,
      selectable: true,
      select: this.handleDateSelect.bind(this),
      // dateClick: this.handleDateClick.bind(this),
      progressiveEventRendering: true
    }
  }


  private handleDateSelect(selectInfo: DateSelectArg) {
    if (selectInfo.view.type !== 'dayGridMonth') {
      return; // Solo actuar si no es un clic en 'dayGridMonth'
    }
    // // Open modal
    this.openEventDialogAfterSelect(selectInfo);
    console.log(selectInfo);
  }


  private openEventDialogAfterSelect(selectInfo: DateSelectArg){
    const dialogRef = this.dialog.open(EventDialogComponent, {
      width: '250px',
      height: '500px',
      data: {
        title: "",
        start: selectInfo.startStr,
        end: selectInfo.endStr,
      }
    });

    dialogRef.afterClosed().subscribe((data: EventData) => {
      this.closeDialogEvent(data);
    });
  }

  private closeDialogEvent(data: EventData){
    this.fullCalendar.getApi().addEvent({
      id: "",
      title: data.title,
      start: data.start,
      end: data.end
    });
    console.log(data);
  }

}
