import { FileDTO } from "../../services/dto/attachment/file-dto.interface";

export interface PostData{
  title: string;
  body: string;
  files: FileDTO[];
}
