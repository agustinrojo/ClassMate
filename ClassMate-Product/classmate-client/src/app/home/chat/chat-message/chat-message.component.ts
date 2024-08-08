import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-chat-message',
  templateUrl: './chat-message.component.html',
  styleUrls: ['./chat-message.component.css']
})
export class ChatMessageComponent{
  @Input() public isSender!: boolean;
  @Input() public content!: string;
  @Input() public timestamp!: Date;
  @Input() public showDate!: boolean;
}
