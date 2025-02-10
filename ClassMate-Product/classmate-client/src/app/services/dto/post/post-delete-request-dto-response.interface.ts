import { FileDTO } from "../attachment/file-dto.interface";
import { DeleteRequestDTOResponse } from "../delete-request/delete-request-response.dto";
import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface PostDeleteRequestDTOResponse{
  id: number;
  forumId: number;
  author: UserDisplayDTO;
  title: string;
  body: string;
  creationDate: Date;
  files: FileDTO[];
  hasBeenEdited: boolean;
  valoration: number;
  commentCount: number;
  deleteRequests: DeleteRequestDTOResponse[];
}
