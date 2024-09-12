import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA  } from '@angular/material/dialog';
import { EventData } from '../../interfaces/calendar/eventEntity-dialog/eventEntity-data.interface';
import { EventDataResult } from '../../interfaces/calendar/eventEntity-dialog/eventEntity-data-result.interface';



@Component({
  selector: 'app-eventEntity-dialog',
  templateUrl: './eventEntity-dialog.component.html',
  styleUrl: './eventEntity-dialog.component.css'
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
