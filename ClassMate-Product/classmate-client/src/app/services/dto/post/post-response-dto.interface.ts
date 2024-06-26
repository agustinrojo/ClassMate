import { FileDTO } from "../attachment/file-dto.interface";

export interface PostResponseDTO{
  id: number;
  forumId: number;
  authorId: number;
  title: string;
  body: string;
  creationDate: Date;
  files: FileDTO[];
  valoration: number;
  likedByUser: boolean;
  dislikedByUser: boolean;
}
