import { Component, OnInit, AfterViewChecked, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { delay } from 'rxjs';
import { User } from './../../../auth/dto/user-dto.interface';
import { UserProfileService } from '../../../services/user-profile.service';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { ChatService } from '../../../services/chat.service';
import { ChatRoomOutputDTO } from '../../../services/dto/chat/chatroom/chatroom-output-dto.interface';
import { ChatMessageOutputDTO } from '../../../services/dto/chat/chat-message/chat-message-output-dto.interface';
import { ChatMessageInputDTO } from '../../../services/dto/chat/chat-message/chat-message-input-dto.interface';
import { UserProfileResponseDTO } from '../../../services/dto/user-profile/user-profile-response-dto.interface';

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrl: './chat-container.component.css'
})
export class ChatContainerComponent implements OnInit, AfterViewChecked {
  public searchQuery: string = "";
  public searchResults: UserProfileResponseDTO[] = [];
  public knownUsers: UserProfileResponseDTO[] = [];
  public messagesList: ChatMessageOutputDTO[] = [];
  public selectedUser: UserProfileResponseDTO | undefined;
  public newMessagesUserIds: Set<number> = new Set();
  public chatroomIds: number[] = [];
  public messageForm!: FormGroup;
  public loggedUser!: User;

  @ViewChild('chatMessages') private chatMessagesContainer!: ElementRef;

  constructor(
    private _userProfileService: UserProfileService,
    private _authService: AuthServiceService,
    private _chatService: ChatService,
    private _fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loggedUser = this._authService.getUser();
    this.initMessageForm();
    this.getChatrooms();
    this.listenMessages();
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    try {
      this.chatMessagesContainer.nativeElement.scrollTop = this.chatMessagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error(err);
    }
  }

  public onSearchChange($event: Event) {
    const target = $event.target as HTMLInputElement;
    this.searchQuery = target ? target.value : '';
    if (this.searchQuery.length > 2) {
      this._userProfileService.searchChatUserByNickname(this.searchQuery).subscribe((resp: UserProfileResponseDTO[]) => {
        console.log(resp);
        this.searchResults = resp;
        this.searchResults = this.searchResults.filter((u: UserProfileResponseDTO) => u.userId != this.loggedUser.id);
      });
    } else {
      this.searchResults = [];
    }
  }

  public sendMessage() {
    let newMessage: ChatMessageInputDTO = {
      senderId: this.loggedUser.id,
      receiverId: this.selectedUser!.userId,
      content: this.messageForm.get("messageInput")!.value
    };
    this.messagesList.push({
      senderId: newMessage.senderId,
      receiverId: newMessage.receiverId,
      chatId: "",
      content: newMessage.content,
      timeStamp: new Date()
    });

    this._chatService.sendMessage(newMessage);
    this.messageForm.get("messageInput")!.reset();
    if (!this.checkKnownUser(newMessage.receiverId)) {
      this.getNewUser(newMessage.receiverId);
      delay(3000);
      this.refreshChatrooms();
    }
  }

  public setSelectedUser(receiver: UserProfileResponseDTO) {
    this.selectedUser = receiver;
    this.readMessage(receiver.userId);
    this.searchQuery = "";
    this.searchResults = [];
    this.getMessages(receiver.userId);
  }

  private listenMessages() {
    this._chatService.getMessageSubject().subscribe((message: ChatMessageOutputDTO) => {
      console.log(message);
      this.getUnknownUsers(message);
      this.moveUserToTop(message.senderId);
      this.addUserAsNewMessage(message.senderId);
      this.messagesList.push(message);
    });
  }

  private moveUserToTop(userId: number) {
    const userIndex = this.knownUsers.findIndex(user => user.userId === userId);
    if (userIndex > -1) {
      const [user] = this.knownUsers.splice(userIndex, 1);
      this.knownUsers.unshift(user);
    }
  }

  private addUserAsNewMessage(userId: number) {
    this.newMessagesUserIds.add(userId);
  }

  private readMessage(userId: number) {
    if (this.newMessagesUserIds.has(userId)) {
      this.newMessagesUserIds.delete(userId);
    }
  }

  private getChatrooms() {
    this._chatService.getChatroomsBySender().subscribe((chatrooms: ChatRoomOutputDTO[]) => {
      chatrooms.forEach((chatroom: ChatRoomOutputDTO) => {
        this.chatroomIds.push(chatroom.id);
      });
      this.getKnownUsers();
    });
  }

  private getUnknownUsers(message: ChatMessageOutputDTO) {
    let unknownUserIds: number | undefined = undefined;

    if (!this.checkKnownUser(message.senderId) && message.senderId != this.loggedUser.id) {
      console.log(message.senderId);
      unknownUserIds = message.senderId;
    }

    if (unknownUserIds != undefined) {
      this._userProfileService.findMultipleUsers([unknownUserIds]).subscribe((users: UserProfileResponseDTO[]) => {
        this.knownUsers.push(...users);
      }, err => {
        console.log(err);
      });
    }
  }

  private checkKnownUser(userId: number): boolean {
    return this.knownUsers.some((user: UserProfileResponseDTO) => user.userId === userId);
  }

  private getKnownUsers() {
    if (this.chatroomIds.length > 0) {
      this._chatService.getKnownUsers(this.chatroomIds).subscribe((userProfiles: UserProfileResponseDTO[]) => {
        this.knownUsers = userProfiles;
      }, err => {
        console.log(err);
      });
    }
  }

  private getNewUser(userId: number) {
    this._userProfileService.findUserProfileSearchById(userId).subscribe((u: UserProfileResponseDTO) => {
      console.log(u);
      this.knownUsers.unshift(u);
      this.setSelectedUser(u);
    }, err => {
      console.log(err);
    });
  }

  private initMessageForm() {
    this.messageForm = this._fb.group({
      messageInput: ["", Validators.required, []]
    });
  }

  private getMessages(receiverId: number) {
    this._chatService.loadMessages(this.loggedUser.id, receiverId).subscribe((messages: ChatMessageOutputDTO[]) => {
      console.log(messages);
      this.messagesList = messages;
    }, err => {
      console.log(err);
    });
  }

  private refreshChatrooms() {
    this._chatService.getChatroomsBySender().subscribe((chatrooms: ChatRoomOutputDTO[]) => {
      let chatroomIds: number[] = [];
      chatrooms.forEach((chatroom: ChatRoomOutputDTO) => {
        chatroomIds.push(chatroom.id);
      });
      console.log(chatroomIds);
      this._authService.setNewChatroomIds(chatroomIds);
    });
  }
}
