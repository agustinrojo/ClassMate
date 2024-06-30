import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserProfileData } from '../../../home/interfaces/user-profile-data.interface';

@Injectable({providedIn: 'root'})
export class UserProfileStateService {
  private userProfileDataSubject = new BehaviorSubject<UserProfileData | null>(null);
  private userProfileData$ = this.userProfileDataSubject.asObservable();
  constructor() { }

  public setUserProfile(data: UserProfileData){
    this.userProfileDataSubject.next(data)
  }

  public getUserProfile(): Observable<UserProfileData | null> {
    return this.userProfileData$;
  }
}
