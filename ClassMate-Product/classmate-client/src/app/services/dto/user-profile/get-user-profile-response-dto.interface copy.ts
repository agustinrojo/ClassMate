import { ProfilePhotoDTO } from "../attachment/profile-photo-dto.interface";

export interface GetUserProfileResponseDTO {
  userId: number;
  nickname: string;
  name: string;
  legajo: string;
  profilePhoto: ProfilePhotoDTO;
  description: string;
  forumsSubscribed: number[];
  likesAmmount: number;
  dislikesAmmount: number;
}
