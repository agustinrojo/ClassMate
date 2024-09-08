import { NotificationDTO } from "./notification-dto.interface";

export interface CommentNotificationDTO extends NotificationDTO {
  postId: number;
  commentId: number;
  forumId: number;
  title: string;
}
