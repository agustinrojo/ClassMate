export interface CommentDTO{
  id: number;
  postId: number;
  authorId: number;
  body: string;
  creationDate: Date;
}
