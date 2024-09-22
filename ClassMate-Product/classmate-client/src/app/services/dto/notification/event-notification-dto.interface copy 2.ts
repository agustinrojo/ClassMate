import { NotificationDTO } from "./notification-dto.interface";

export interface EventNotificationDTO extends NotificationDTO {
  title: string;
  startDate: Date;
}
