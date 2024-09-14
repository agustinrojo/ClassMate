import { NgModule } from '@angular/core';
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
    NotificationComponent

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CalendarModule
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
    NotificationComponent

  ]
})
export class SharedModule { }
