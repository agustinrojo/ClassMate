import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarComponent } from './calendar/calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { FormsModule } from '@angular/forms';
import { EventDialogComponent } from './event-dialog/event-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';


@NgModule({
  declarations: [
    CalendarComponent,
    EventDialogComponent
  ],
  imports: [
    CommonModule,
    FullCalendarModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
  ],
  exports: [
    CalendarComponent
  ]
})
export class CalendarModule { }
