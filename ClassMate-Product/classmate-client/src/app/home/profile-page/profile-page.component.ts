import { Component, OnInit } from '@angular/core';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';
import { User } from '../../auth/dto/user-dto.interface';
import { UserData } from '../../auth/interfaces/user-data.interface';
import { UserProfileService } from '../../services/user-profile.service';
import { AuthServiceService } from '../../auth/auth-service.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit{

  public userProfile!: UserProfileResponseDTO;
  public userProfilePhoto!: string;
  public userData!: UserData;

  constructor( private _userProfileService: UserProfileService,
               private _authService: AuthServiceService
  ){}

  ngOnInit(): void {
    this.getUserProfile();
    this.userData = this._authService.getUserData();
  }

  private getUserProfile(){
    this._userProfileService.getUserProfile().subscribe((resp: UserProfileResponseDTO) => {
      this.userProfile = resp;
      this.getUserProfilePhoto(this.userProfile.profilePhoto.photoId);
    },
    err => {
      console.log(err);
      return
    })
  }

  private getUserProfilePhoto(photoId: number) {
    this._userProfileService.getUserProfilePhoto(photoId).subscribe((resp: Blob) => {
      this.userProfilePhoto = URL.createObjectURL(resp);
    },
  err => {
    console.log(err);
  })
  }
}
