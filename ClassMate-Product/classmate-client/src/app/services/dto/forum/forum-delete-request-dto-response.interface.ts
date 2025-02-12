import { DeleteRequestDTOResponse } from "../delete-request/delete-request-response.dto";

export interface ForumDeleteRequestDTOResponse {
    id: number;
    title: string;
    description: string;
    creationDate: Date;
    hasBeenEdited: boolean;
    deleteRequests: DeleteRequestDTOResponse[];
}
