import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ForumDTO } from './dto/forum/forum-dto.interface';
import { ForumApiResponseDTO } from './dto/forum/forum-api-response-dto.interface';
import { ForumRequestDTO } from './dto/forum/create/forum-request-dto.interface';
import { AuthServiceService } from '../auth/auth-service.service';
import { ForumExistsDTO } from './dto/forum/forum-exists-dto.interface';

@Injectable({providedIn: 'root'})
export class ForumService {
  private baseUrl : String = "http://localhost:8080/api/forums";
  constructor(private http: HttpClient, private _authService: AuthServiceService) { }

  public getForums() : Observable<ForumDTO[]> {
    return this.http.get<ForumDTO[]>(`${this.baseUrl}`);
  }

  public getForumById(id: string) : Observable<ForumApiResponseDTO> {
    return this.http.get<ForumApiResponseDTO>(`${this.baseUrl}/${id}`);
  }

  public forumExists(forumId: number): Observable<ForumExistsDTO> {
    return this.http.get<ForumExistsDTO>(`${this.baseUrl}/exists/${forumId}`);
  }

  public addMember(forumId: number, memberId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${forumId}/members/${memberId}`, {});
  }

  public saveForum(forum: ForumRequestDTO, creatorId: number) : Observable<ForumDTO> {
    return this.http.post<ForumDTO>(`${this.baseUrl}?creatorId=${creatorId}`, forum);
  }

  public deleteForum(forumId : number) {
    let userId: number = this._authService.getUserId();
    return this.http.delete<void>(`${this.baseUrl}/${forumId}?userId=${userId}`);
  }

}
