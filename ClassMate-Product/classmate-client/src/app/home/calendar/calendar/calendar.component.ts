import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import bootstrapPlugin from '@fullcalendar/bootstrap'
import esLocale from '@fullcalendar/core/locales/es';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CalendarComponent implements OnInit {

  calendarOptions?: CalendarOptions;

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
      dateClick: this.handleDateClick.bind(this),
      events: '/api/events', // TODO: Traer eventos del back
      editable: true,
      selectable: true,
    }
  }



  handleDateClick(arg: any) {
    console.log('Date clicked:', arg.dateStr);
    // Logic to add events
  }
}
