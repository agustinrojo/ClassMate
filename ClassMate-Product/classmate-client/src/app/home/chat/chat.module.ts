import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatContainerComponent } from './chat-container/chat-container.component';
import { ChatMessageComponent } from './chat-message/chat-message.component';
import { ChatUserComponent } from './chat-user/chat-user.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DateMarkerComponent } from './date-marker/date-marker.component';



@NgModule({
  declarations: [
    ChatContainerComponent,
    ChatMessageComponent,
    ChatUserComponent,
    DateMarkerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ChatModule { }
