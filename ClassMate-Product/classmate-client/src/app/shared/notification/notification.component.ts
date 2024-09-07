import { Component, OnDestroy, OnInit } from '@angular/core';
import { NotificationDTO } from '../../services/dto/notification/notification-dto.interface';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { CommentNotificationDTO } from '../../services/dto/notification/comment-notification-dto.interface copy';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent implements OnInit, OnDestroy {

  notifications: NotificationDTO[] = [];
  private notificationSubscription!: Subscription;

  constructor(private _notificationService: NotificationService) {}

  ngOnInit(): void {
    // Subscribe to real-time notifications
    this.notificationSubscription = this._notificationService.getNotificationSubject().subscribe((notification: NotificationDTO) => {
      this.handleNotification(notification);
    });

    this._notificationService.loadNotifications().subscribe((notifications: NotificationDTO[]) => {
      this.notifications = notifications;
      console.log("Loaded notifications", notifications);
    }, (error) => {
      console.error("Failed to load notifications", error);
    }
  );
  }

  ngOnDestroy(): void {
    if(this.notificationSubscription) {
      this.notificationSubscription.unsubscribe();
    }
  }

  // Handle notifications based on their type
  handleNotification(notification: NotificationDTO): void {
    switch (notification.type) {
      case "COMMENT":
        const commentNotification = notification as CommentNotificationDTO;
        console.log("Comment notification received", commentNotification);
        this.notifications.push(commentNotification);
        break;
        //TODO: Otros case
        default:
          console.warn("Unknown notification type", notification);
    }
  }

}
