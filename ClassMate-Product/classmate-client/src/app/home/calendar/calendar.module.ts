import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarComponent } from './calendar/calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';


@NgModule({
  declarations: [
    CalendarComponent
  ],
  imports: [
    CommonModule,
    FullCalendarModule
  ],
  exports: [
    CalendarComponent
  ]
})
export class CalendarModule { }
