import { FileDTO } from "../attachment/file-dto.interface";
import { CommentDTOResponse } from "../comment/comment-response-dto.interface";

export interface PostAPIResponseDTO {
  id: number;
  forumId: number;
  authorId: number;
  title: string;
  body: string;
  lastMilestone: number;
  hasBeenEdited: boolean | null;
  creationDate: Date;
  commentDTOS: CommentDTOResponse[];
  files: FileDTO[];
}
