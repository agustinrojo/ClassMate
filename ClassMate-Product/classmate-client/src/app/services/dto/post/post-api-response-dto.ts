import { FileDTO } from "../attachment/file-dto.interface";
import { CommentDTOResponse } from "../comment/comment-response-dto.interface";
import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface PostAPIResponseDTO {
  id: number;
  forumId: number;
  author: UserDisplayDTO;
  title: string;
  body: string;
  lastMilestone: number;
  hasBeenEdited: boolean | null;
  creationDate: Date;
  commentDTOS: CommentDTOResponse[];
  files: FileDTO[];
  commentCount: number;
}
