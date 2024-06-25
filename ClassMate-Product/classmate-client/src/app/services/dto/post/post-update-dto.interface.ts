export interface PostUpdateDTO {
  title: string;
  body: string;
  filesToAdd: File[];
  fileIdsToRemove: number[];
}
