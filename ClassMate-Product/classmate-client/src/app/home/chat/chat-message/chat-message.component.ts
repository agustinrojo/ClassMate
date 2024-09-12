import { Component, Input } from '@angular/core';
import { ChatMessageOutputDTO } from '../../../services/dto/chat/chat-message/chat-message-output-dto.interface';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { FileService } from '../../../services/file.service';
import { FileDownloadEvent } from '../../interfaces/file-download-eventEntity.interface';

@Component({
  selector: 'app-chat-message',
  templateUrl: './chat-message.component.html',
  styleUrls: ['./chat-message.component.css']
})
export class ChatMessageComponent{
  @Input() public isSender!: boolean;
  @Input() public message! : ChatMessageOutputDTO;
  @Input() public showDate!: boolean;

  constructor(private _fileService: FileService){

  }

  public downloadFile(file: FileDownloadEvent) {
    this._fileService.downloadFile(file.fileId).subscribe((blob: Blob) => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = file.fileName;
      document.body.appendChild(a);  // Añadir el elemento al DOM
      a.click();
      URL.revokeObjectURL(objectUrl);
      document.body.removeChild(a);  // Eliminar el elemento del DOM
    }, err => {
      console.log(err);
    })
  }
}
