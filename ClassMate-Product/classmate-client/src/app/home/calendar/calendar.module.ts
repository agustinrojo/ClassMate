import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarComponent } from './calendar/calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { FormsModule } from '@angular/forms';
import { EventDialogComponent } from './eventEntity-dialog/eventEntity-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import {MatMenuModule} from '@angular/material/menu';
import { DetailDialogComponent } from './detail-dialog/detail-dialog.component';


@NgModule({
  declarations: [
    CalendarComponent,
    EventDialogComponent,
    DetailDialogComponent
  ],
  imports: [
    CommonModule,
    FullCalendarModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatMenuModule
  ],
  exports: [
    CalendarComponent
  ]
})
export class CalendarModule { }
