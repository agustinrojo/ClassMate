export interface ChatMessageOutputDTO {
  senderId: number;
  receiverId: number;
  chatId: string;
  content: string;
  timeStamp: Date;
  showDate?: boolean;
  dateLabel?: string;
}
