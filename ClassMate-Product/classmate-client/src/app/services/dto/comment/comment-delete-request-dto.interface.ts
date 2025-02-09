import { FileDTO } from "../attachment/file-dto.interface";
import { DeleteRequestDTOResponse } from "../delete-request/delete-request-response.dto";
import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface CommentDeleteRequestDTO {
    id: number;
    postId: number;
    forumId: number;
    author: UserDisplayDTO;
    body: string;
    creationDate: Date;
    files: FileDTO[];
    hasBeenEdited: boolean;
    valoration: number;
    deleteRequests: DeleteRequestDTOResponse[];
}
