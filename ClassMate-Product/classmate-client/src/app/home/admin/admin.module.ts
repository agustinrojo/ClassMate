import { CommonModule } from '@angular/common';
import { ReportsComponent } from './reports/reports.component';
import { CommentReportsComponent } from './comment-reports/comment-reports.component';
import { PostPageModule } from '../post-page/post-page.module';
import { ReportMessageComponent } from './report-message/report-message.component';
import { PostReportsComponent } from './post-reports/post-reports.component';
import { NgModule } from '@angular/core';
import { SingleForumPageModule } from '../single-forum-page/single-forum-page.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    ReportsComponent,
    CommentReportsComponent,
    ReportMessageComponent,
    PostReportsComponent
  ],
  imports: [
    CommonModule,
    PostPageModule,
    SingleForumPageModule,
    FormsModule
  ]
})
export class AdminModule { }
