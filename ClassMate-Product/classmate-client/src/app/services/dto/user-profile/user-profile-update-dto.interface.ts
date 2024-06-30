import { ProfilePhotoUpdateDTO } from "./profile-photo-update-dto.interface";

export interface UserProfileUpdateDTO{
  nickname: string;
  profilePhotoUpdateDTO: ProfilePhotoUpdateDTO;
  description: string;
}
