import { Component, OnInit } from '@angular/core';
import { UserProfileResponseDTO } from '../../../services/dto/user-profile/user-profile-response-dto.interface';
import { User } from '../../../auth/dto/user-dto.interface';
import { UserData } from '../../../auth/interfaces/user-data.interface';
import { UserProfileService } from '../../../services/user-profile.service';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserProfileStateService } from '../../../services/dto/state-services/user-profile-state.service';
import { UserProfileData } from '../../interfaces/user-profile-data.interface';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit{


  public userProfile!: UserProfileResponseDTO;
  public userProfilePhotoURL!: string;
  public userProfilePhoto!: Blob;
  public userData!: UserData;
  public userId!: string;
  public loggedUserId!: string;

  constructor( private _userProfileService: UserProfileService,
               private _userProfileStateService: UserProfileStateService,
               private _authService: AuthServiceService,
               private _activatedRoute: ActivatedRoute,
               private _router: Router
  ){}

  ngOnInit(): void {
    this.loggedUserId = this._authService.getUserId().toString();
    this.userData = this._authService.getUserData();
    this.userId = this._activatedRoute.snapshot.paramMap.get("id" || "0")!

    this.getUserProfile();

  }

  public navigateToEditProfile() {
    let userProfileData: UserProfileData = {
      nickname: this.userProfile.nickname,
      description: this.userProfile.description,
      profilePhoto: new File([this.userProfilePhoto], "Foto Actual"),
      photoId: this.userProfile.profilePhoto.photoId
    }
    this._userProfileStateService.setUserProfile(userProfileData);
    this._router.navigate([`profile/${this.userId}/edit`]);
  }

  private getUserProfile(){
    console.log(this.userId)
    this._userProfileService.getUserProfile(this.userId).subscribe((resp: UserProfileResponseDTO) => {
      console.log(resp);
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
      this.userProfilePhoto = resp;
      this.userProfilePhotoURL = URL.createObjectURL(resp);
    },
  err => {
    console.log(err);
  })
  }
}
