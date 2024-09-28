import { ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NotificationDTO } from '../../services/dto/notification/notification-dto.interface';
import { Subscription } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { CommentNotificationDTO } from '../../services/dto/notification/comment-notification-dto.interface copy';
import { Router } from '@angular/router';
import { MessageNotificationDTO } from '../../services/dto/notification/message-notification-dto.interface';
import { MilestoneNotificationDTO } from '../../services/dto/notification/milestone-notification-dto.interface copy 2';
import { EventNotificationDTO } from '../../services/dto/notification/event-notification-dto.interface copy 2';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent implements OnInit, OnDestroy {

  notifications: NotificationDTO[] = [];
  private notificationSubscription!: Subscription;
  private currentPage: number = 0;
  private pageSize: number = 10;
  private totalPages: number = 0;
  public isLoading: boolean = false; // Track if data is being loaded
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

   // Load initial notifications
    this.loadNotifications(this.currentPage);

  }

  loadNotifications(page: number): void {
    this.isLoading = true;
    this._notificationService.loadNotifications(page, this.pageSize).subscribe(response => {
      this.notifications = [...this.notifications, ...response.content]; // Append new notifications
      this.totalPages = response.totalPages;
      this.isLoading = false; // Reset loading flag
      this.cdRef.markForCheck(); // Trigger change detection
    }, error => {
      console.error("Failed to load notifications", error);
      this.isLoading = false;
      this.showNotifications = false;
    });
  }

  loadMore(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadNotifications(this.currentPage);
    }
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
    switch (notification.type) {
      case "COMMENT": {
        const commentNotification = notification as CommentNotificationDTO;
        const truncatedTitle = commentNotification.title.length > 20
          ? commentNotification.title.substring(0, 20) + "..."
          : commentNotification.title;
        return `Recibiste un comentario en tu post: ${truncatedTitle}! "`;
      }

      case "MESSAGE": {
        const messageNotification = notification as MessageNotificationDTO;
        return `Recibiste un mensaje de ${messageNotification.senderName}!`;
      }

      case "MILESTONE": {
        const milestoneNotification = notification as MilestoneNotificationDTO;
        const milestoneType = milestoneNotification.milestoneType

        if (milestoneType === "COMMENT") {
          return `¡Tu comentario tiene ${milestoneNotification.milestone} valoraciones positivas!`;
        } else if (milestoneType == "POST") {
          return `¡Tu post tiene ${milestoneNotification.milestone} valoraciones positivas!`;
        } else return "";
      }

      case "EVENT": {
        const eventNotification = notification as EventNotificationDTO;
        const truncatedTitle = eventNotification.title.length > 20
        ? eventNotification.title.substring(0, 20) + "..."
        : eventNotification.title;
        return `¡Hoy tenés un evento! ${truncatedTitle}`;
      }

      default:
        return 'New notification';
    }
  }


  goToNotification(notification: NotificationDTO): void {
    switch (notification.type) {
      case "COMMENT":
        const commentNotification = notification as CommentNotificationDTO;
        this._router.navigate([`/forum/${commentNotification.forumId}/post/${commentNotification.postId}`]);
        break;

      case "MESSAGE":
        // const messageNotification = notification as MessageNotificationDTO;
        this._router.navigate(['/chat']);
        break;

      case "MILESTONE":
        const milestoneNotification = notification as MilestoneNotificationDTO;
        this._router.navigate([`/forum/${milestoneNotification.forumId}/post/${milestoneNotification.postId}`]);
        break;

      case "EVENT":
        // const eventNotification = notification as EventNotificationDTO;
        this._router.navigate(['/calendar']);
        break;

      default:
        console.warn('Unhandled notification type:', notification.type);
        break;
    }
  }


  // Handle notifications based on their type
  handleNotification(notification: NotificationDTO): void {
    switch (notification.type) {
      case "COMMENT":
        const commentNotification = notification as CommentNotificationDTO;
        this.notifications.unshift(commentNotification);
        break;
      case "MESSAGE":
        const messageNotification = notification as MessageNotificationDTO;
        this.notifications.unshift(messageNotification);
        break;
      case "MILESTONE":
        const milestoneNotification = notification as MilestoneNotificationDTO;
        this.notifications.unshift(milestoneNotification);
        break;
        case "EVENT":
          const eventNotification = notification as EventNotificationDTO;
          this.notifications.unshift(eventNotification);
          break;
      default:
          console.warn("Unknown notification type", notification);
    }
    this.cdRef.markForCheck();
  }

  // Hide the panel when clicked outside
  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent): void {
    const clickedInside = (event.target as HTMLElement).closest('.notification-panel');
    const clickedBell = (event.target as HTMLElement).closest('.notification-bell');

    if (!clickedInside && !clickedBell) {
      this.showNotifications = false;
      this.markNotificationsAsSeen();
    }
  }

    // Infinite scroll logic, don't ask
    @HostListener('window:scroll', [])
    onScroll(): void {
      if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight && !this.isLoading) {
        // When the user has scrolled to the bottom of the page
        if (this.currentPage < this.totalPages - 1) {
          this.currentPage++;
          this.loadNotifications(this.currentPage);
        }
      }
    }

  trackByNotificationId(index: number, notification: NotificationDTO): number {
    return notification.id;
  }

}
