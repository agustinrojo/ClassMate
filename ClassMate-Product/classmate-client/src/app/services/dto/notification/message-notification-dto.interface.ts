import { NotificationDTO } from "./notification-dto.interface";

export interface MessageNotificationDTO extends NotificationDTO {
  senderId: number;
  senderName: string;
}
