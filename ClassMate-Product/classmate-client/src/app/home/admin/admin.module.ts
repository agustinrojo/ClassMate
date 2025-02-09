import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportsComponent } from './reports/reports.component';
import { CommentReportsComponent } from './comment-reports/comment-reports.component';
import { PostPageModule } from '../post-page/post-page.module';
import { ReportMessageComponent } from './report-message/report-message.component';



@NgModule({
  declarations: [
    ReportsComponent,
    CommentReportsComponent,
    ReportMessageComponent
  ],
  imports: [
    CommonModule,
    PostPageModule
  ]
})
export class AdminModule { }
