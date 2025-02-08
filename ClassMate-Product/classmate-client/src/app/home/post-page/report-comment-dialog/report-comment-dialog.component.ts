import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-report-comment-dialog',
  templateUrl: './report-comment-dialog.component.html',
  styleUrl: './report-comment-dialog.component.css'
})

// interface Message {
//   message:string;
// }

export class ReportCommentDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ReportCommentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public message: string
  ) {}

  report() {
    this.dialogRef.close(this.message);
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }


}
