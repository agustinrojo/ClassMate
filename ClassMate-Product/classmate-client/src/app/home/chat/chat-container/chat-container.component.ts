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
import { delay } from 'rxjs';

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
  public newMessagesUserIds: Set<number> = new Set();
  public chatroomIds: number[] = [];
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
    this.getChatrooms();
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
    if(!this.checkKnownUser(newMessage.receiverId)){

      this.getNewUser(newMessage.receiverId);
      delay(3000)
      this.refreshChatrooms();
    }
  }



  public setSelectedUser(receiver: UserProfileSearchDTO){
    this.selectedUser = receiver;
    this.readMessage(receiver.userId);
    this.searchQuery = "";
    this.searchResults = [];
    this.getMessages(receiver.userId);
  }

  private listenMessages(){
    this._chatService.getMessageSubject().subscribe((messages: ChatMessageOutputDTO[]) => {
      console.log(messages);
      this.getUnknownUsers(messages);


      messages.forEach((message: ChatMessageOutputDTO) => {
        if (!this.checkKnownUser(message.senderId) && message.senderId != this.loggedUser.id) {
          this.getNewUser(message.senderId);
        } else {
          this.moveUserToTop(message.senderId);
          this.addUserAsNewMessage(message.senderId);
        }
      });

      this.messagesList = messages;
    })
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

  private readMessage(userId: number){
    if(this.newMessagesUserIds.has(userId)){
      this.newMessagesUserIds.delete(userId);
    }
  }

  private getChatrooms(){
    this._chatService.getChatroomsBySender().subscribe((chatrooms: ChatRoomOutputDTO[]) => {
      chatrooms.forEach((chatroom: ChatRoomOutputDTO) => {
        this.chatroomIds.push(chatroom.id);
      })
      this.getKnownUsers();
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

  private getKnownUsers() {
    console.log("entro a getKnownUsers()", this.chatroomIds.length)
    if(this.chatroomIds.length > 0){

      this._chatService.getKnownUsers(this.chatroomIds).subscribe((userProfiles :  UserProfileSearchDTO[]) => {
        console.log(userProfiles);
        this.knownUsers = userProfiles;
      },
    err => {
      console.log(err);
    });
    }
  }

  private getNewUser(userId: number){
    this._userProfileService.findUserProfileSearchById(userId).subscribe((u: UserProfileSearchDTO) => {
      console.log(u);
      this.knownUsers.unshift(u);
      this.setSelectedUser(u);
    },
    err => {
      console.log(err);
    })
  }

  private initMessageForm(){
    this.messageForm = this._fb.group({
      messageInput: ["", Validators.required, []]

    })
  }

  private getMessages(receiverId: number){
    this._chatService.loadMessages(this.loggedUser.id, receiverId).subscribe((messages: ChatMessageOutputDTO[]) => {
      this.messagesList = messages;
    },
    err => {
      console.log(err);
    })
  }

  private refreshChatrooms(){
    this._chatService.getChatroomsBySender().subscribe((chatrooms: ChatRoomOutputDTO[]) => {
      let chatroomIds: number[] = [];
      chatrooms.forEach((chatroom: ChatRoomOutputDTO) => {
        chatroomIds.push(chatroom.id);
      })
      console.log(chatroomIds);
      this._authService.setNewChatroomIds(chatroomIds);
    })
  }

}
