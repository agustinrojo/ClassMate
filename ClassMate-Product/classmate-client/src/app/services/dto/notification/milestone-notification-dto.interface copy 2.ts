import { NotificationDTO } from "./notification-dto.interface";

export interface MilestoneNotificationDTO extends NotificationDTO {
  postId: number;
  forumId: number;
  milestone: number;
  title: string;
  milestoneType: string;
}
