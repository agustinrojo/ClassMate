import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CompatClient, IMessage, Stomp } from '@stomp/stompjs';
import { AuthServiceService } from '../auth/auth-service.service';
import SockJS from 'sockjs-client';
import { ChatMessageOutputDTO } from './dto/chat/chat-message/chat-message-output-dto.interface';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { ChatMessageInputDTO } from './dto/chat/chat-message/chat-message-input-dto.interface';
import { ChatRoomOutputDTO } from './dto/chat/chatroom/chatroom-output-dto.interface';
import { UserProfileResponseDTO } from './dto/user-profile/user-profile-response-dto.interface';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private baseWSUrl: string = "http://localhost:8088/ws";
  private apiBaseUrl: string = "http://localhost:8088/api/messages";
  private socket!: WebSocket;
  private stompClient!: CompatClient;
  private loggedUserId!: number;
  private messageSubject: Subject<ChatMessageOutputDTO> = new Subject<ChatMessageOutputDTO>();

  constructor(
    private http:HttpClient,
    private _authService: AuthServiceService,
  ) {
    this.loggedUserId = this._authService.getUserId();


    this.initWSConnection();
  }

  public initWSConnection(){
    this.socket = new SockJS(this.baseWSUrl);
    this.stompClient = Stomp.over(this.socket);
    if(this.loggedUserId){
      this.subscribeToChat();
    }

  }

  public subscribeToChat(){
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(`/user/${this.loggedUserId}/queue/messages`, (message: IMessage) => {
        const messageContent: ChatMessageOutputDTO = JSON.parse(message.body);
        console.log("messageContent", messageContent);
        this.messageSubject.next(messageContent);
      })
    })
  }

  public sendMessage(chatMessage: ChatMessageInputDTO){
    this.stompClient.send(
      `/app/chat.sendMessage`,
      {},
      JSON.stringify(chatMessage)
    );
  }

  public getMessageSubject(): Observable<ChatMessageOutputDTO> {
    return this.messageSubject.asObservable();
  }

  public loadMessages(senderId: number, receiverId: number): Observable<ChatMessageOutputDTO[]> {
    return this.http.get<ChatMessageOutputDTO[]>(`${this.apiBaseUrl}/${senderId}/${receiverId}`);
  }

  public getChatroomsBySender(): Observable<ChatRoomOutputDTO[]>{
    return this.http.get<ChatRoomOutputDTO[]>(`${this.apiBaseUrl}/chatroom/${this.loggedUserId}`);
  }

  public getKnownUsers(chatroomIds : number[] ): Observable<UserProfileResponseDTO[]> {
    let accessToken : string = this._authService.getAccessToken();
    // let params: HttpParams = new HttpParams();
    let chatroomIdsParam = chatroomIds.join(',');
    let url = `${this.apiBaseUrl}/chatrooms?chatroomId=${chatroomIdsParam}&token=${accessToken}`;

    return this.http.get<UserProfileResponseDTO[]>(url, { responseType: 'json' });
  }

  // public clearMessages(){
  //   this.messageSubject = new BehaviorSubject<ChatMessageOutputDTO[]>([]);
  // }

}
