import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserProfileResponseDTO } from './dto/user-profile/user-profile-response-dto.interface';
import { Observable } from 'rxjs';
import { ForumDTO } from './dto/forum/forum-dto.interface';

@Injectable({
  providedIn: 'root'
})
export class TaggingService {
  private usersBaseUrl: string = "http://localhost:8080/api/profiles";
  private forumsBaseUrl : String = "http://localhost:8080/api/forums";

  constructor(private http: HttpClient) { }

  public searchUserByNickname( nicknameSubstr: string, page: number = 0, size: number = 10 ) : Observable<UserProfileResponseDTO[]>{
    return this.http.get<UserProfileResponseDTO[]>(`${this.usersBaseUrl}/search/${nicknameSubstr}?page=${page}&size=${size}`);
  }

  public getForumsByTitle(title: string, page: number = 0, size: number = 10) : Observable<ForumDTO[]> {
    return this.http.get<ForumDTO[]>(`${this.forumsBaseUrl}/search?title=${title}&page=${page}&size=${size}`);
  }

}
