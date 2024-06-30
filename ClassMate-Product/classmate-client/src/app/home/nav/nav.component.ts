import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from '../../auth/auth-service.service';
import { Router } from '@angular/router';
import { User } from '../../auth/dto/user-dto.interface';
import { UserProfileService } from '../../services/user-profile.service';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit{

  public userProfile!: UserProfileResponseDTO;
  public userProfilePhoto!: string | null;

  constructor(private _authService: AuthServiceService,
              private _router: Router,
              private _userProfileService: UserProfileService){}

  ngOnInit(): void {
    this.getUserProfile();
    this._userProfileService.setUserProfile(this.userProfile, this.userProfilePhoto!);
  }

  public logout(){
    this._router.navigate(["login"]);
    this._authService.logout();
  }

  public navigateToForums(){
    console.log("click")
    this._router.navigate(["forums"]);
  }

  public navigateToMain(){
    console.log("click")
    this._router.navigate(["home/main"]);
  }

  public navigateToProfile() {
    this._router.navigate([`profile/${this._authService.getUserId()}`])
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
