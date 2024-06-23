import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FileDTO } from '../../services/dto/attachment/file-dto.interface';
import { FileDownloadEvent } from '../../home/interfaces/file-download-event.interface';

@Component({
  selector: 'app-file-badge',
  templateUrl: './file-badge.component.html',
  styleUrl: './file-badge.component.css'
})
export class FileBadgeComponent implements OnInit{

  @Input() public file!: FileDTO;
  @Input() public canModify!: boolean;
  @Output() public removeEvent: EventEmitter<void> = new EventEmitter<void>();
  @Output() public downloadFileEvent: EventEmitter<FileDownloadEvent> = new EventEmitter<FileDownloadEvent>();

  ngOnInit(): void {

  }

  public downloadFile(){
    let fileDownloadEvent: FileDownloadEvent = {
      fileId: this.file.id,
      fileName: this.file.originalFilename
    }
    this.downloadFileEvent.emit(fileDownloadEvent);
  }

  public removeFile() {
   this.removeEvent.emit();
  }
}
