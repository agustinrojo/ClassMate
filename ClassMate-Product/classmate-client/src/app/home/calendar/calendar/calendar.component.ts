import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import bootstrapPlugin from '@fullcalendar/bootstrap'
import esLocale from '@fullcalendar/core/locales/es';
import { FullCalendarComponent } from '@fullcalendar/angular';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  encapsulation: ViewEncapsulation.None
})
export class CalendarComponent implements OnInit {

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
      dateClick: this.handleDateClick.bind(this),
      progressiveEventRendering: true
    }
  }


  handleDateSelect(selectInfo: any) {
    this.selectedEvent = {
      id: '',
      title: '',
      start: selectInfo.startStr,
      end: selectInfo.endStr
    };
    // Open modal
    this.openEventModal();
  }

  openEventModal(): void {
    const modal = document.getElementById('eventModal');
    if (modal) {
      modal.style.display = 'block'; // Show modal
    }
  }

  closeEventModal(): void {
    const modal = document.getElementById('eventModal');
    if (modal) {
      modal.style.display = 'none'; // Hide modal
    }
  }

  saveEvent(): void {
    if (this.calendarOptions && this.calendarOptions.events) {
      this.eventsArray.push({...this.selectedEvent})
      console.log(this.eventsArray);

    }
    this.closeEventModal();
  }

  handleDateClick(arg: any): void {
    console.log('Date clicked:', arg.dateStr);
    // Logic to add events
  }
}
