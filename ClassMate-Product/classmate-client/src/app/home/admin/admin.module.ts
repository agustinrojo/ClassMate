import { CommonModule } from '@angular/common';
import { ReportsComponent } from './reports/reports.component';
import { CommentReportsComponent } from './comment-reports/comment-reports.component';
import { PostPageModule } from '../post-page/post-page.module';
import { ReportMessageComponent } from '../../shared/report-message/report-message.component';
import { PostReportsComponent } from './post-reports/post-reports.component';
import { NgModule } from '@angular/core';
import { SingleForumPageModule } from '../single-forum-page/single-forum-page.module';
import { FormsModule } from '@angular/forms';
import { ForumReportsComponent } from './forum-reports/forum-reports.component';
import { SharedModule } from "../../shared/shared.module";



@NgModule({
  declarations: [
    ReportsComponent,
    CommentReportsComponent,
    PostReportsComponent,
    ForumReportsComponent
  ],
  imports: [
    CommonModule,
    PostPageModule,
    SingleForumPageModule,
    FormsModule,
    SharedModule
  ],
  exports: [
  ]
})
export class AdminModule { }
