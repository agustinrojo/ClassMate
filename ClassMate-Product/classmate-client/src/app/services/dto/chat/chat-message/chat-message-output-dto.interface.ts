import { FileDTO } from "../../attachment/file-dto.interface";

export interface ChatMessageOutputDTO {
  senderId: number;
  receiverId: number;
  chatId: string;
  content: string;
  timeStamp: Date;
  attachment?: FileDTO;
  showDate?: boolean;
  dateLabel?: string;
}
