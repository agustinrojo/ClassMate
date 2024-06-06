import { CommentDTO } from "../comment/comment-dto.interface";

export interface PostAPIResponseDTO {
  id: number;
  forumId: number;
  authorId: number;
  title: string;
  body: string;
  creationDate: Date;
  commentDTOS: CommentDTO[];
}
