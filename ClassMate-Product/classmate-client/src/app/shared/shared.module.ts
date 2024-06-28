import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileBadgeComponent } from './file-badge/file-badge.component';
import { CapitalizePipe } from './pipes/capitalize.pipe';
import { FileSizePipe } from './pipes/file-size.pipe';
import { TruncatePipe } from './pipes/truncate.pipe';
import { ValorationComponent } from './valoration/valoration.component';
import { LinkifyPipe } from './pipes/linkify.pipe';



@NgModule({
  declarations: [
    FileBadgeComponent,
    CapitalizePipe,
    FileSizePipe,
    TruncatePipe,
    ValorationComponent,
    LinkifyPipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    FileBadgeComponent,
    ValorationComponent,
    CapitalizePipe,
    FileSizePipe,
    TruncatePipe,
    LinkifyPipe
  ]
})
export class SharedModule { }
