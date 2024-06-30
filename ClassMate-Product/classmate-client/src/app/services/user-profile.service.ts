import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { UserProfileRequestDTO } from './dto/user-profile/user-profile-request-dto.interface';
import { Observable } from 'rxjs';
import { UserProfileResponseDTO } from './dto/user-profile/user-profile-response-dto.interface';

@Injectable({providedIn: 'root'})
export class UserProfileService {
  private userId: number;
  private baseUrl: string = "http://localhost:8080/api/profiles";
  private userProfile?: UserProfileResponseDTO;
  private userProfilePhoto?: string | null;

  constructor(
    private http: HttpClient,
    private _authService: AuthServiceService
  ) {
    this.userId = this._authService.getUserId();
  }

  public getUserProfile(): Observable<UserProfileResponseDTO>{
    return this.http.get<UserProfileResponseDTO>(`${this.baseUrl}/${this.userId}`);
  }

  public getUserProfilePhoto( photoId: number ): Observable<Blob>{

    return this.http.get(`${this.baseUrl}/photo/${photoId}`, {responseType: 'blob'})
  }

  public saveUserProfile( userProfileReq:UserProfileRequestDTO ): Observable<UserProfileResponseDTO>{
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    let formData = this.mapUserProfileReqToFormData(userProfileReq);
    return this.http.post<UserProfileResponseDTO>(`${this.baseUrl}`, formData, { headers });
  }

  public setUserProfile(userProfile: UserProfileResponseDTO, photoUrl: string){
    this.userProfile = userProfile;
    this.userProfilePhoto = photoUrl;
  }

  private mapUserProfileReqToFormData(req: UserProfileRequestDTO): FormData{
    let formData: FormData = new FormData();
    formData.append("userId", this.userId.toString());
    formData.append("nickname", req.nickname);
    formData.append("profilePhoto", req.profilePhoto);
    formData.append("description", req.description);
    return formData;
  }




}
