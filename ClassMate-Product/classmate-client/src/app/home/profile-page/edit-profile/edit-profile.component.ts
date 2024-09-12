import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { UserProfileData } from '../../interfaces/user-profile-data.interface';
import { UserProfileStateService } from '../../../services/dto/state-services/user-profile-state.service';
import { mapFileToFIleDTO } from '../../../mappers/mapFileToFileDTO.mapper';
import { ProfilePhotoUpdateDTO } from '../../../services/dto/user-profile/profile-photo-update-dto.interface';
import { UserProfileUpdateDTO } from '../../../services/dto/user-profile/user-profile-update-dto.interface';
import { UserProfileService } from '../../../services/user-profile.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent implements OnInit{


  public editProfileForm!: FormGroup;
  public disableBtn: boolean = false;
  public showErr: any = false;
  public currentProfile!: UserProfileData | null;
  public currentProfilePhoto!: File | null;
  public currentProfilePhotoDTO!: FileDTO | null;
  public userId!: string;

  constructor(private _userProfileStateService: UserProfileStateService,
              private _userProfileService: UserProfileService,
              private _fb: FormBuilder,
              private _router: Router,
              private _activatedRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    this.loadUserProfile();
    this.userId = this._activatedRoute.snapshot.paramMap.get("id")!;
    this.editProfileForm = this._fb.group({
      nickname: [this.currentProfile?.nickname, Validators.required, []],
      description: [this.currentProfile?.description, [], []],
      profilePhoto: [this.currentProfilePhoto, Validators.required, []]
    })
  }

  public onFileChange(eventEntity: any) {
    if (eventEntity.target.files.length > 0) {
      const files = eventEntity.target.files;
        this.currentProfilePhoto = files[0];
        this.currentProfilePhotoDTO = mapFileToFIleDTO(files[0]);
        this.editProfileForm.get("profilePhoto")!.setValue(this.currentProfilePhoto);
    }
  }

  public onSubmit() {
    let profilePhotoUpdate: ProfilePhotoUpdateDTO = {
      photoToAdd: this.currentProfilePhoto!,
      photoIdToRemove: null
    }

    let userProfileUpdateDTO: UserProfileUpdateDTO = {
      nickname: this.editProfileForm.get("nickname")?.value,
      description: this.editProfileForm.get("description")?.value,
      profilePhotoUpdateDTO: profilePhotoUpdate
    }

    this.updateProfile(userProfileUpdateDTO);
  }

  public goBack() {
    this._router.navigate([`profile/${this.userId}`])
  }

  public removePhoto() {
    this.currentProfilePhoto = null;
    this.currentProfilePhotoDTO = null;
    this.editProfileForm.get("profilePhoto")?.reset();
  }

  private loadUserProfile(){
    this._userProfileStateService.getUserProfile().subscribe((userProfile: UserProfileData | null) => {
      this.currentProfile = userProfile;
      this.currentProfilePhoto = userProfile!.profilePhoto;
      this.currentProfilePhotoDTO = mapFileToFIleDTO(this.currentProfilePhoto);
    })
  }

  private updateProfile(updateReq: UserProfileUpdateDTO){
    this._userProfileService.updateUserProfile(updateReq).subscribe(() => {
      this.goBack();
    },
    err => {
      console.log(err);
      this.showErr = true;
    })
  }

}
