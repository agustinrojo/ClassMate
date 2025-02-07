import { Role } from "../enums/role.enum";

export interface User{
  id: number;
  firstName: string;
  lastName: string;
  legajo: string;
  carrera: string;
  role: Role;
  email:string;
  forumsSubscribed: number[];
  forumsAdmin: number[];
  forumsCreated: number[];
  chatroomIdsIn: number[];
  synced: boolean;
}
