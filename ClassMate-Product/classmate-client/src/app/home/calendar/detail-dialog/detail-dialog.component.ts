import {ChangeDetectionStrategy, Component, Inject, inject, viewChild} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
} from '@angular/material/dialog';
import {MatMenuModule, MatMenuTrigger} from '@angular/material/menu';
import { EventData } from '../../interfaces/calendar/eventEntity-dialog/eventEntity-data.interface';
import { EventDataResult } from '../../interfaces/calendar/eventEntity-dialog/eventEntity-data-result.interface';

@Component({
  selector: 'app-detail-dialog',
  templateUrl: './detail-dialog.component.html',
  styleUrl: './detail-dialog.component.css'
})
export class DetailDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: EventData,
              public dialogRef: MatDialogRef<DetailDialogComponent>) {}

  public onDelete() {
    let result: EventDataResult = {
      eventId: this.data.id,
      delete: true
    }

    this.dialogRef.close(result);
  }

  public onEdit() {
    let result: EventDataResult = {
      edit: true,
      title: this.data.title,
      description: this.data.description,
      start: this.data.start.toString(),
      end: this.data.end.toString()
    }
    this.dialogRef.close(result);
  }


}
