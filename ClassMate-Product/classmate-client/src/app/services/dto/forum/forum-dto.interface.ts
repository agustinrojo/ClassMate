import { UserDisplayDTO } from "../user-display/user-display.interface";

export interface ForumDTO {
  id : number;
  title : string;
  description : string;
  creator : UserDisplayDTO;
  members : UserDisplayDTO[]
  admins : UserDisplayDTO[]
  creationDate : Date;
  hasBeenEdited: boolean;
}
