import { FileDTO } from "../../attachment/file-dto.interface";

export interface ChatMessageInputDTO {
  senderId: number;
  receiverId: number;
  content: string;
  attachment?: FileDTO;
}
