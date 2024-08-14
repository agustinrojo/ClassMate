import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatContainerComponent } from './chat-container/chat-container.component';
import { ChatMessageComponent } from './chat-message/chat-message.component';
import { ChatUserComponent } from './chat-user/chat-user.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DateMarkerComponent } from './date-marker/date-marker.component';
import { TaggingComponent } from '../../shared/tagging/tagging.component';
import { SharedModule } from '../../shared/shared.module';



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
    ReactiveFormsModule,
    SharedModule
  ]
})
export class ChatModule { }
