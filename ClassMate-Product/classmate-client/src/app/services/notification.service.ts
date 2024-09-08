import { AuthServiceService } from '../auth/auth-service.service';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Injectable } from '@angular/core';
import { CompatClient, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { NotificationDTO } from './dto/notification/notification-dto.interface';
import { Observable, Subject } from 'rxjs';
import { NotificationUpdateDTO } from './dto/notification/notification-update-dto.interface';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient!: CompatClient;
  private baseWSUrl : string = "http://localhost:8089/ws-notifications";
  private apiBaseUrl: string = "http://localhost:8089/api/notifications";
  private socket!: WebSocket;
  private loggedUserId!: number;
  private notificationSubject: Subject<NotificationDTO> = new Subject<NotificationDTO>();

  constructor(
    private http: HttpClient,
    private authService: AuthServiceService,

  ) {
    this.loggedUserId = this.authService.getUserId();
    this.initWSConnection();
   }

   public initWSConnection() {
    this.socket = new SockJS(this.baseWSUrl);  // Connect to the backend WebSocket
    this.stompClient = Stomp.over(this.socket);  // Use StompJS over SockJS

    if (this.loggedUserId) {
      this.subscribeToNotifications();
    }
  }
  public subscribeToNotifications() {
    this.stompClient.connect({}, () => {
      // Subscribe to notifications for the logged in user
      this.stompClient.subscribe(`/topic/notifications/${this.loggedUserId}`, (message) => {
        const notification: NotificationDTO = JSON.parse(message.body); // Parse the notification
        this.notificationSubject.next(notification); // Push notification to subscribers
      });
    }, (error: any) => {
      console.error("WebSocket connection error:", error);
    });
  }

  // Observable to subscribe to notifications in components
  public getNotificationSubject(): Observable<NotificationDTO> {
    return this.notificationSubject.asObservable();
  }

  // HTTP: Fetch all notifications for the logged-in user
  public loadNotifications(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.apiBaseUrl}/user/${this.loggedUserId}?page=${page}&size=${size}`);
  }


  // HTTP: Mark notification as seen
  public updateNotification(notificationUpdate: NotificationUpdateDTO): Observable<void> {
    return this.http.put<void>(`${this.apiBaseUrl}/update`, notificationUpdate);
  }

}
