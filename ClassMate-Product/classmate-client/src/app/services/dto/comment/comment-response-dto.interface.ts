import { FileDTO } from "../attachment/file-dto.interface";

export interface CommentDTOResponse{
  id: number;
  postId: number;
  authorId: number;
  body: string;
  creationDate: Date;
  files: FileDTO[];
  hasBeenEdited: boolean;
  valoration: number;
  likedByUser: boolean;
  dislikedByUser: boolean;
}
