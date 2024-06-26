import { FileDTO } from "../attachment/file-dto.interface";

export interface CommentDTOResponse{
  id: number;
  postId: number;
  authorId: number;
  body: string;
  hasBeenEdited: boolean;
  creationDate: Date;
  files: FileDTO[];
}
