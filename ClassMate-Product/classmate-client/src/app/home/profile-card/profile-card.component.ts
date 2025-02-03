import { Component, Input, OnInit } from '@angular/core';
import { UserProfileService } from '../../services/user-profile.service';
import { ProfilePhotoDTO } from '../../services/dto/attachment/profile-photo-dto.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.css'
})
export class ProfileCardComponent implements OnInit {
  @Input() public userId!: number;
  @Input() public username!: string;
  @Input() public name!: string;
  @Input() public description!: string;
  @Input() public profilePhotoDTO!: ProfilePhotoDTO;
  public userProfilePhoto!: string | null;

  constructor(private _userProfileService: UserProfileService,
              private _router: Router
  ){}

  ngOnInit(): void {
    this.getUserProfilePhoto(this.profilePhotoDTO.photoId);
  }


  public navigateToUserProfile() {
    this._router.navigate([`profile/${this.userId}`]);
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
