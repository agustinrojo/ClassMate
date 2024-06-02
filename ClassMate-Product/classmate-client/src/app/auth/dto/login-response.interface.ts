import { User } from "./user-dto.interface";

export interface LoginResponse{
  accessToken: string;
  refreshToken: string;
  user: User
}
