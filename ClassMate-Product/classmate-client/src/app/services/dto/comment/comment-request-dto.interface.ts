export interface CommentDTORequest{
  id: number;
  postId: number;
  authorId: number;
  body: string;
  creationDate: Date;
  files: any[];
}
