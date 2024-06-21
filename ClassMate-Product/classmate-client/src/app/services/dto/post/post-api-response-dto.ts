import { CommentDTOResponse } from "../comment/comment-response-dto.interface";

export interface PostAPIResponseDTO {
  id: number;
  forumId: number;
  authorId: number;
  title: string;
  body: string;
  creationDate: Date;
  commentDTOS: CommentDTOResponse[];
}
