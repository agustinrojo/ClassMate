import { Component, Input } from '@angular/core';
import { ChatUserDTO } from '../../../services/dto/chat/chat-user/chat-user-dto.interface';

@Component({
  selector: 'app-chat-user',
  templateUrl: './chat-user.component.html',
  styleUrl: './chat-user.component.css'
})
export class ChatUserComponent {
  @Input() public user!: ChatUserDTO;
}
