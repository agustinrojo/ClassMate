export interface ForumDTO {
  id : number;
  title : string;
  description : string;
  creatorId : number;
  memberIds : number[]
  adminIds : number[]
  creationDate : Date;
}
