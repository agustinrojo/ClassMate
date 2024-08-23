import { Component, OnInit } from '@angular/core';
import { CalendarOptions, Calendar } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { FullCalendarComponent } from '@fullcalendar/angular';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
})
export class CalendarComponent implements OnInit {

  calendarOptions?: CalendarOptions;

  ngOnInit(): void {

    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [
        dayGridPlugin,
        interactionPlugin
      ],
      headerToolbar: {
        left: 'prev, next today',
        center: 'title',
        right: ''
      },
      themeSystem: 'standard',
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
