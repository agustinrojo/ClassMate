import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { UserProfileRequestDTO } from './dto/user-profile/user-profile-request-dto.interface';
import { Observable } from 'rxjs';
import { UserProfileResponseDTO } from './dto/user-profile/user-profile-response-dto.interface';
import { UserProfileData } from '../home/interfaces/user-profile-data.interface';
import { UserProfileUpdateDTO } from './dto/user-profile/user-profile-update-dto.interface';
import { UserProfileWithRoleDTO } from './dto/forum/user/user-profile-with-role-dto.interface';

@Injectable({providedIn: 'root'})
export class UserProfileService {
  private userId: number;
  private baseUrl: string = "http://localhost:8080/api/profiles";

  constructor(
    private http: HttpClient,
    private _authService: AuthServiceService
  ) {
    this.userId = this._authService.getUserId();
  }



  public getUserProfile( userId: string ): Observable<UserProfileResponseDTO>{
    return this.http.get<UserProfileResponseDTO>(`${this.baseUrl}/${userId}`);
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

  public updateUserProfile( userProfileUpdateDTO : UserProfileUpdateDTO ): Observable<void> {
    let formData: FormData = this.mapUserProfileUpdateToFormData(userProfileUpdateDTO);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.put<void>(`${this.baseUrl}/${this.userId}`, formData, { headers });
  }

  public searchChatUserByNickname( nicknameSubstr: string, page: number = 0, size: number = 10 ) : Observable<UserProfileResponseDTO[]>{
    return this.http.get<UserProfileResponseDTO[]>(`${this.baseUrl}/search/${nicknameSubstr}?page=${page}&size=${size}`);
  }

  public findMultipleUsers(userIds: number[]): Observable<UserProfileResponseDTO[]> {
    let params: HttpParams = new HttpParams();
    userIds.forEach((userId: number) => {
      params = params.append("userId", userId);
    });
    return this.http.get<UserProfileResponseDTO[]>(`${this.baseUrl}/search/findMultiple` , { params });
  }

  public findUserProfileSearchById(userId: number): Observable<UserProfileResponseDTO> {
    return this.http.get<UserProfileResponseDTO>(`${this.baseUrl}/search/profileSearch/${userId}`);
  }

  public searchUsersInForum(forumId: number, query: string){
    return this.http.get<UserProfileWithRoleDTO[]>(`${this.baseUrl}/search/forumMembers/${forumId}?q=${query}`);
  }

  private mapUserProfileReqToFormData(req: UserProfileRequestDTO): FormData{
    let formData: FormData = new FormData();
    formData.append("userId", this.userId.toString());
    formData.append("nickname", req.nickname);
    formData.append("profilePhoto", req.profilePhoto);
    formData.append("description", req.description);
    return formData;
  }

  private mapUserProfileUpdateToFormData(userProfileUpdate: UserProfileUpdateDTO) {
    let formData: FormData = new FormData();

    formData.append("nickname", userProfileUpdate.nickname);
    formData.append("description", userProfileUpdate.description);
    formData.append("profilePhotoUpdateDTO.photoToAdd", userProfileUpdate.profilePhotoUpdateDTO.photoToAdd);

    return formData;
  }



}
