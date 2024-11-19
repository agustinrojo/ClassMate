import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { NotificationPreferenceService } from '../../../services/notification-preference.service';
import { UserProfileService } from '../../../services/user-profile.service';
import { debounceTime, Subject } from 'rxjs';
import { NotificationPreferenceUpdateDTO } from '../../../services/dto/notification/notification-preference-update-dto.interface copy 2';
import { NotificationPreferenceDTO } from '../../../services/dto/notification/notification-preference-dto.interface copy';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notification-preferences',
  templateUrl: './notification-preferences.component.html',
  styleUrl: './notification-preferences.component.css'
})
export class NotificationPreferencesComponent implements OnInit{

  private preferenceUpdateSubject: Subject<void> = new Subject<void>();
  private loggedUserId!: number;

  // Notification preferences
  public preferences: NotificationPreferenceUpdateDTO = {
    commentNotificationEnabled: false,
    likeNotificationEnabled: false,
    messageNotificationEnabled: false,
    eventNotificationEnabled: false
  };

  constructor(private _authService: AuthServiceService,
              private _notificationPreferenceService: NotificationPreferenceService,
              private _userProfileService: UserProfileService,
              private _router: Router
  ){  }

  ngOnInit(): void {
    this.loggedUserId = this._authService.getUserId();
    this.preferenceUpdateSubject.pipe(debounceTime(150)).subscribe(() => {
      this.savePreferences();
    });
    this.loadUserPreferences();
  }

  public goBack(){
    this._router.navigate([`profile/${this.loggedUserId}`]);
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
