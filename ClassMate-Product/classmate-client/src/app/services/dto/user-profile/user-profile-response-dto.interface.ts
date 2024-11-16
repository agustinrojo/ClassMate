import { ProfilePhotoDTO } from "../attachment/profile-photo-dto.interface";

export interface UserProfileResponseDTO {
  userId: number;
  nickname: string;
  name: string;
  profilePhoto: ProfilePhotoDTO;
  description: string;
  forumsSubscribed: number[];
}
