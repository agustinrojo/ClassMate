import { Component, OnInit } from '@angular/core';
import { UserProfileResponseDTO } from '../../../services/dto/user-profile/user-profile-response-dto.interface';
import { UserProfileService } from '../../../services/user-profile.service';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { NotificationPreferenceService } from '../../../services/notification-preference.service';

import { ActivatedRoute, Router } from '@angular/router';
import { UserData } from '../../../auth/interfaces/user-data.interface';
import { UserProfileStateService } from '../../../services/dto/state-services/user-profile-state.service';
import { debounceTime, Subject } from 'rxjs';
import { NotificationPreferenceUpdateDTO } from '../../../services/dto/notification/notification-preference-update-dto.interface copy 2';
import { NotificationPreferenceDTO } from '../../../services/dto/notification/notification-preference-dto.interface copy';


@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {
  public userProfile!: UserProfileResponseDTO;
  public userProfilePhotoURL!: string;
  public userProfilePhoto!: Blob;
  public userData!: UserData;
  public userId!: string;
  public loggedUserId!: string;

  private preferenceUpdateSubject: Subject<void> = new Subject<void>();

  // Notification preferences
  public preferences: NotificationPreferenceUpdateDTO = {
    commentNotificationEnabled: false,
    likeNotificationEnabled: false,
    messageNotificationEnabled: false,
    eventNotificationEnabled: false
  };


  constructor(
    private _userProfileService: UserProfileService,
    private _userProfileStateService: UserProfileStateService,
    private _authService: AuthServiceService,
    private _notificationPreferenceService: NotificationPreferenceService,
    private _activatedRoute: ActivatedRoute,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this.loggedUserId = this._authService.getUserId().toString();
    this.userData = this._authService.getUserData();
    this.userId = this._activatedRoute.snapshot.paramMap.get("id" || "0")!;

    this.getUserProfile();

    // Debounce preference updates to avoid spamming the backend
    this.preferenceUpdateSubject.pipe(debounceTime(150)).subscribe(() => {
      this.savePreferences();
    });

    this.loadUserPreferences();
  }

  public navigateToEditProfile() {
    // Navigation logic here
  }

  private getUserProfile() {
    this._userProfileService.getUserProfile(this.userId).subscribe((resp: UserProfileResponseDTO) => {
      this.userProfile = resp;
      this.getUserProfilePhoto(this.userProfile.profilePhoto.photoId);
    });
  }

  private getUserProfilePhoto(photoId: number) {
    this._userProfileService.getUserProfilePhoto(photoId).subscribe((resp: Blob) => {
      this.userProfilePhoto = resp;
      this.userProfilePhotoURL = URL.createObjectURL(resp);
    });
  }

  // Load current preferences
  private loadUserPreferences() {
    this._notificationPreferenceService.getUserPreferences(Number(this.loggedUserId)).subscribe((preferences: NotificationPreferenceDTO) => {
      this.preferences = preferences;
    });
  }

  // Trigger the debounce subject on preference change
  public updatePreferences() {
    console.log("Updating preferences:", this.preferences); // Add this log to inspect preferences
    this.preferenceUpdateSubject.next();
  }


  // Save updated preferences
  private savePreferences() {
    this._notificationPreferenceService.updateUserPreferences(Number(this.loggedUserId), this.preferences).subscribe(() => {
      console.log("Preferences updated successfully", this.preferences);
    });
  }
}
