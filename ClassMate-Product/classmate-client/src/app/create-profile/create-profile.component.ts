import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FileDTO } from '../services/dto/attachment/file-dto.interface';
import { mapFileToFIleDTO } from '../mappers/mapFileToFileDTO.mapper';
import { UserProfileRequestDTO } from '../services/dto/user-profile/user-profile-request-dto.interface';
import { UserProfileService } from '../services/user-profile.service';
import { UserProfileResponseDTO } from '../services/dto/user-profile/user-profile-response-dto.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html',
  styleUrl: './create-profile.component.css'
})
export class CreateProfileComponent implements OnInit{
  public createProfileForm!: FormGroup;
  public selectedPhoto!: File;
  public selectedPhotoDTO?: FileDTO;
  public disableBtn: boolean = false;
  public showErr: boolean = false;

  constructor(
    private _fb: FormBuilder,
    private _userProfileService: UserProfileService,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this.createProfileForm = this._fb.group({
      nickname: ["", Validators.required, []],
      profilePhoto: ["", Validators.required, []],
      description: ["", [], []]
    })
  }

  public onFileChange(eventEntity: any) {
    if (eventEntity.target.files.length > 0) {
      const files = eventEntity.target.files;
        this.selectedPhoto = files[0];
        this.selectedPhotoDTO = mapFileToFIleDTO(files[0]);
    }
  }

  public onSubmit(){
    this.disableBtn = true;
    this.showErr = false;
    let userProfileReq: UserProfileRequestDTO = {
      userId: 0,
      nickname: this.createProfileForm.get("nickname")!.value,
      profilePhoto: this.selectedPhoto,
      description: this.createProfileForm.get("description")!.value
    }

    this._userProfileService.saveUserProfile(userProfileReq).subscribe((resp: UserProfileResponseDTO) => {
      this.handleSubmitSuccess();
    },
    err => {
      this.handleSubmitError(err);
    })
  }

  public removeFile(){
    this.selectedPhotoDTO = undefined;
    this.createProfileForm.get("profilePhoto")?.reset();
  }

  private handleSubmitSuccess(){
    this._router.navigate(["home/main"])
  }

  private handleSubmitError(err: any){
    this.disableBtn = false
    this.showErr = true;
    console.log(err);
  }





}
