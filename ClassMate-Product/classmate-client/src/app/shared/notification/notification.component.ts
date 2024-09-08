import { ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NotificationDTO } from '../../services/dto/notification/notification-dto.interface';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { CommentNotificationDTO } from '../../services/dto/notification/comment-notification-dto.interface copy';
import { Router } from '@angular/router';
import { MessageNotificationDTO } from '../../services/dto/notification/message-notification-dto.interface';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent implements OnInit, OnDestroy {

  notifications: NotificationDTO[] = [];
  private notificationSubscription!: Subscription;
  public showNotifications: boolean = false;

  constructor(
    private _notificationService: NotificationService,
    private _router: Router,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Subscribe to real-time notifications
    this.notificationSubscription = this._notificationService.getNotificationSubject().subscribe((notification: NotificationDTO) => {
      this.handleNotification(notification);
    });

    this._notificationService.loadNotifications().subscribe((notifications: NotificationDTO[]) => {
      this.notifications = notifications;
    }, (error) => {
      console.error("Failed to load notifications", error);
      this.showNotifications = false;
    }
  );

  }

  ngOnDestroy(): void {
    if(this.notificationSubscription) {
      this.notificationSubscription.unsubscribe();
    }
  }

  toggleNotificationPanel(): void {
    this.showNotifications = !this.showNotifications;

    // If notifications are shown, mark unseen notifications as seen
    if (!this.showNotifications) {
      this.markNotificationsAsSeen();
    }
  }

  // Mark unseen notifications as seen and update the backend
  private markNotificationsAsSeen(): void {
    const unseenNotifications: NotificationDTO[] = this.notifications.filter(notification => !notification.isSeen);

    if (unseenNotifications.length > 0) {
      unseenNotifications.forEach(notification => {
        notification.isSeen = true; // Mark as seen in the UI
        this._notificationService.updateNotification({ id: notification.id, isSeen: true }).subscribe(
          () => {
            console.log(`Notification ${notification.id} marked as seen`);
          },
          (error) => {
            console.error(`Failed to mark notification ${notification.id} as seen`, error);
          }
        );
      });
    }
  }

  hasUnreadNotifications(): boolean {
    return this.notifications.some(notification => !notification.isSeen);
  }

  getNotificationText(notification: NotificationDTO): string {
    if (notification.type === "COMMENT") {
      return 'Alguien comentó en tu post!';
    }
    if (notification.type === "MESSAGE") {
      const messageNotification = notification as MessageNotificationDTO;
      return `Recibiste un mensaje de ${messageNotification.senderName}!`;
    }
    return 'New notification';
  }

  goToNotification(notification: NotificationDTO): void {
    if (notification.type === "COMMENT") {
      const commentNotification = notification as CommentNotificationDTO;
      this._router.navigate([`/forum/${commentNotification.forumId}/post/${commentNotification.postId}`]);
    } else if (notification.type === "MESSAGE") {
      const messageNotification = notification as MessageNotificationDTO;
      this._router.navigate(['/chat']);
    }
  }

  // Handle notifications based on their type
  handleNotification(notification: NotificationDTO): void {
    switch (notification.type) {
      case "COMMENT":
        const commentNotification = notification as CommentNotificationDTO;
        // console.log("Comment notification received", commentNotification);
        this.notifications.push(commentNotification);
        break;
      case "MESSAGE":
        const messageNotification = notification as MessageNotificationDTO;
        this.notifications.push(messageNotification);
        break;
        //TODO: Otros case
        default:
          console.warn("Unknown notification type", notification);
    }
    this.cdRef.detectChanges();
  }

  // Hide the panel when clicked outside
  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent): void {
    const clickedInside = (event.target as HTMLElement).closest('.notification-panel');
    const clickedBell = (event.target as HTMLElement).closest('.notification-bell');

    if (!clickedInside && !clickedBell) {
      this.showNotifications = false;
    }
  }

  trackByNotificationId(index: number, notification: NotificationDTO): number {
    return notification.id;
  }

}
