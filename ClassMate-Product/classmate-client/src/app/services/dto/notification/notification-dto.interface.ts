export interface NotificationDTO {
  id: number;
  userId: number;
  isSeen: boolean;
  type: string;
  creationDate: Date
}
