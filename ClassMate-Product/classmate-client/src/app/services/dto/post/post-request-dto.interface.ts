export interface PostRequestDTO{
  id: number;
  forumId: number;
  authorId: number;
  title: string;
  body: string;
  creationDate: Date;
  files: File[];
}
