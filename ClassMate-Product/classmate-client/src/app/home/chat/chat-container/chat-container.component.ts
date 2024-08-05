import { User } from './../../../auth/dto/user-dto.interface';
import { Component, OnInit } from '@angular/core';
import { UserProfileService } from '../../../services/user-profile.service';
import { UserProfileSearchDTO } from '../../../services/dto/user-profile/user-profile-search-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { ChatService } from '../../../services/chat.service';
import { ChatRoomOutputDTO } from '../../../services/dto/chat/chatroom/chatroom-output-dto.interface';
import { ChatMessageOutputDTO } from '../../../services/dto/chat/chat-message/chat-message-output-dto.interface';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChatMessageInputDTO } from '../../../services/dto/chat/chat-message/chat-message-input-dto.interface';

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrl: './chat-container.component.css'
})
export class ChatContainerComponent implements OnInit{
  public searchQuery: string = "";
  public searchResults: UserProfileSearchDTO[] = [];
  public knownUsers: UserProfileSearchDTO[] = [];
  public messagesList: ChatMessageOutputDTO[] = [];
  public selectedUser: UserProfileSearchDTO | undefined;
  public messageForm!: FormGroup;
  public loggedUser!: User;


  constructor(
    private _userProfileService:UserProfileService,
    private _authService: AuthServiceService,
    private _chatService: ChatService,
    private _fb: FormBuilder
  ){

  }

  ngOnInit(): void {
    this.loggedUser = this._authService.getUser();
    this.initMessageForm();
    this.getKnownUsers();
    this.listenMessages();
  }

  public onSearchChange($event: Event) {
    const target = $event.target as HTMLInputElement;
    this.searchQuery = target ? target.value : '';
    if(this.searchQuery.length > 2){
      this._userProfileService.searchChatUserByNickname(this.searchQuery).subscribe((resp: UserProfileSearchDTO[]) => {
        console.log(resp);
        this.searchResults = resp;
        this.searchResults = this.searchResults.filter((u: UserProfileSearchDTO) => u.userId != this.loggedUser.id);
      })
    } else {
      this.searchResults = [];
    }
  }

  public sendMessage(){

    let newMessage : ChatMessageInputDTO = {
      senderId: this.loggedUser.id,
      receiverId: this.selectedUser!.userId,
      content: this.messageForm.get("messageInput")!.value
    }
    this.messagesList.push({
      senderId: newMessage.senderId,
      receiverId: newMessage.receiverId,
      chatId: "",
      content: newMessage.content,
      timeStamp: new Date()
    })
    this._chatService.sendMessage(newMessage);
    this.messageForm.get("messageInput")!.reset();
  }

  private getKnownUsers() {
    let chatroomIds: number [] = this.loggedUser.chatroomIdsIn;
    if(chatroomIds.length > 0){
      this._chatService.getKnownUsers(chatroomIds).subscribe((userProfiles :  UserProfileSearchDTO[]) => {
        this.knownUsers = userProfiles;
      });
    }
  }

  public setSelectedUser(receiver: UserProfileSearchDTO){
    this.selectedUser = receiver;
    this.searchQuery = "";
    this.searchResults = [];
    this._chatService.loadMessages(this.loggedUser.id, receiver.userId);
  }

  private listenMessages(){
    this._chatService.getMessageSubject().subscribe((messages: ChatMessageOutputDTO[]) => {
      console.log(messages);
      this.getUnknownUsers(messages);
       this.messagesList = messages;
    })
  }

  private getUnknownUsers(messages: ChatMessageOutputDTO[]){
    let unknownUserIds: number[] = [];

    messages.forEach((message: ChatMessageOutputDTO) => {
      if( !this.checkKnownUser(message.senderId)  && message.senderId != this.loggedUser.id){
        console.log(message.senderId);
        unknownUserIds.push(message.senderId);
      }
    })

    if(unknownUserIds.length != 0){
      this._userProfileService.findMultipleUsers(unknownUserIds).subscribe((users: UserProfileSearchDTO[]) => {
        this.knownUsers.push(...users);
      },
      err => {
        console.log(err);
      })
    }
  }

  private checkKnownUser(userId: number): boolean {
    return this.knownUsers.some((user: UserProfileSearchDTO) => user.userId === userId);
  }

  private initMessageForm(){
    this.messageForm = this._fb.group({
      messageInput: ["", Validators.required, []]
    })
  }

}
