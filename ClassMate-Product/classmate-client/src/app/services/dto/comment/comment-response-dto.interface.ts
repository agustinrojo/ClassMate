import { FileDTO } from "../attachment/file-dto.interface";
import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface CommentDTOResponse{
  id: number;
  postId: number;
  forumId: number;
  author: UserDisplayDTO;
  body: string;
  creationDate: Date;
  files: FileDTO[];
  hasBeenEdited: boolean;
  valoration: number;
  likedByUser: boolean;
  dislikedByUser: boolean;
}
