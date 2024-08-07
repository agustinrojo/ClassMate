import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { UserProfileResponseDTO } from '../../../services/dto/user-profile/user-profile-response-dto.interface';
import { UserProfileService } from '../../../services/user-profile.service';

@Component({
  selector: 'app-chat-user',
  templateUrl: './chat-user.component.html',
  styleUrl: './chat-user.component.css'
})
export class ChatUserComponent implements OnInit{

  @Input() public user!: UserProfileResponseDTO;
  @Input() public isSelected!: boolean;
  public userProfilePhotoUrl!: string;

  constructor(
    private cdr: ChangeDetectorRef,
    private _userProfileService: UserProfileService
  ) {}

  ngOnInit(): void {
    if (this.user.profilePhoto) {
      this.getUserProfilePhoto(this.user.profilePhoto.photoId);
    }
  }

  private getUserProfilePhoto(photoId: number) {
    this._userProfileService.getUserProfilePhoto(photoId).subscribe((resp: Blob) => {
      this.userProfilePhotoUrl = URL.createObjectURL(resp);
    },
  err => {
    console.log(err);
  })
  }


}
