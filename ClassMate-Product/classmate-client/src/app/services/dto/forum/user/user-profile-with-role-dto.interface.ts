import { ProfilePhotoDTO } from "../../attachment/profile-photo-dto.interface";

export interface UserProfileWithRoleDTO {
  userId: number;
  nickname: string;
  profilePhoto: ProfilePhotoDTO;
  userType: string;      // "Creator", "Admin", or "Subscriber"
}
