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
import { PostService } from '../../../services/post.service';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { GetUserProfileResponseDTO } from '../../../services/dto/user-profile/get-user-profile-response-dto.interface copy';


@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {
  public userProfile!: GetUserProfileResponseDTO;
  public userProfilePhotoURL!: string;
  public userProfilePhoto!: Blob;
  public userData!: UserData;
  public userId!: string;
  public loggedUserId!: string;
  public posts: PostResponseDTO[] = []
  public hoverText: string = '';


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
    private _authService: AuthServiceService,
    private _notificationPreferenceService: NotificationPreferenceService,
    private _postService: PostService,
    private _activatedRoute: ActivatedRoute,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this.initComponent();
    this._activatedRoute.params.subscribe((params) => {
      this.initComponent();
    });
  }

  private initComponent(){
    this.loggedUserId = this._authService.getUserId().toString();
    this.userData = this._authService.getUserData();
    this.userId = this._activatedRoute.snapshot.paramMap.get("id")!;

    this.getUserProfile();
    // Debounce preference updates to avoid spamming the backend
    this.preferenceUpdateSubject.pipe(debounceTime(150)).subscribe(() => {
      this.savePreferences();
    });

    this.getPosts();
    this.loadUserPreferences();
  }

  public navigateToEditProfile() {
    this._router.navigate(['profile', this.userId, 'edit']);
  }

  public navigateToNotificationPreferences() {
    this._router.navigate(['profile', this.userId, 'notification-preferences']);
  }

  public deletePost(postId: number){
    this._postService.deletePost(postId).subscribe(() => {
      const index = this.posts.findIndex((p: PostResponseDTO) => postId == p.id);
      if(index != -1){
        this.posts.splice(index, 1);
      }
    },
    (err) => {
      console.log(err);
    }
    )
  }

  private getUserProfile() {
    this._userProfileService.getUserProfile(this.userId).subscribe((resp: GetUserProfileResponseDTO) => {
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

  private getPosts(){
    this._postService.getPostsByAuthor(this.userId).subscribe((posts: PostResponseDTO[]) => {
      this.posts = [...posts];
    },
    (err) => {
      console.log(err);
    })
  }

  public getLikesPercentage(): number {
    const total = this.userProfile.likesAmmount + this.userProfile.dislikesAmmount;
    const percentage = total > 0 ? (this.userProfile.likesAmmount / total) * 100 : 50;
    return percentage > 100 ? 100 : percentage;
  }

  public getDislikesPercentage(): number {
    const total = this.userProfile.likesAmmount + this.userProfile.dislikesAmmount;
    const percentage = total > 0 ? (this.userProfile.dislikesAmmount / total) * 100 : 50;
    return percentage > 100 ? 100 : percentage;
  }


}
