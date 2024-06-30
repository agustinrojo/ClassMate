import { ProfilePhotoDTO } from "../attachment/profile-photo-dto.interface";

export interface UserProfileResponseDTO {
  userId: number;
  nickname: string;
  profilePhoto: ProfilePhotoDTO;
  description: string;
}
