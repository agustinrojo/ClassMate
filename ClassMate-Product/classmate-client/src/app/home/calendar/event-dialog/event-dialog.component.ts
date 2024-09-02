import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA  } from '@angular/material/dialog';
import { EventData } from '../../interfaces/calendar/event-dialog/event-data.interface';
import { EventDataResult } from '../../interfaces/calendar/event-dialog/event-data-result.interface';



@Component({
  selector: 'app-event-dialog',
  templateUrl: './event-dialog.component.html',
  styleUrl: './event-dialog.component.css'
})
export class EventDialogComponent {


  constructor(
    public dialogRef: MatDialogRef<EventDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EventData
  ){}

  public onNoClick(): void {
    this.dialogRef.close();
  }

  public deleteEvent() {
    const result: EventDataResult = {
      delete: true,
      eventId: this.data.id!,
  };
  this.dialogRef.close(result);
    }
}
