import { FileDTO } from "../attachment/file-dto.interface";
import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface PostResponseDTO{
  id: number;
  forumId: number;
  author: UserDisplayDTO;
  title: string;
  body: string;
  lastMilestone: number;
  creationDate: Date;
  files: FileDTO[];
  hasBeenEdited: boolean;
  valoration: number;
  likedByUser: boolean;
  dislikedByUser: boolean;
  commentCount: number;
  reportedByUser: boolean;
}
