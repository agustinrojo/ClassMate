import { CommonModule } from '@angular/common';
import { FileBadgeComponent } from './file-badge/file-badge.component';
import { CapitalizePipe } from './pipes/capitalize.pipe';
import { FileSizePipe } from './pipes/file-size.pipe';
import { TruncatePipe } from './pipes/truncate.pipe';
import { ValorationComponent } from './valoration/valoration.component';
import { LinkifyPipe } from './pipes/linkify.pipe';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SearchComponent } from './search/search.component';
import { SharedSidebarComponent } from './shared-sidebar/shared-sidebar.component';
import { TaggingComponent } from './tagging/tagging.component';
import { CalendarModule } from "../home/calendar/calendar.module";
import { NotificationComponent } from './notification/notification.component';
import { ReportCommentDialogComponent } from './report-comment-dialog/report-comment-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { ReportMessageComponent } from './report-message/report-message.component';
import { NgModule } from '@angular/core';




@NgModule({
  declarations: [
    FileBadgeComponent,
    CapitalizePipe,
    FileSizePipe,
    TruncatePipe,
    ValorationComponent,
    LinkifyPipe,
    SearchComponent,
    SharedSidebarComponent,
    TaggingComponent,
    NotificationComponent,
    ReportCommentDialogComponent,
    ReportMessageComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CalendarModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatMenuModule,
],
  exports: [
    FileBadgeComponent,
    ValorationComponent,
    CapitalizePipe,
    FileSizePipe,
    TruncatePipe,
    LinkifyPipe,
    SearchComponent,
    SharedSidebarComponent,
    TaggingComponent,
    NotificationComponent,
    ReportMessageComponent

  ]
})
export class SharedModule { }
