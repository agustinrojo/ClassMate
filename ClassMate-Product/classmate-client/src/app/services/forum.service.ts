import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ForumDTO } from './dto/forum/forum-dto.interface';
import { ForumApiResponseDTO } from './dto/forum/forum-api-response-dto.interface';
import { ForumRequestDTO } from './dto/forum/create/forum-request-dto.interface';
import { AuthServiceService } from '../auth/auth-service.service';
import { ForumExistsDTO } from './dto/forum/forum-exists-dto.interface';
import { IsForumCreatorDTO } from './dto/forum/is-forum-creator-dto.interface';
import { ForumDataSidebar } from './dto/forum/forum-data-dto.interface';
import { ForumStateService } from './dto/state-services/forum-state.service';
import { DeleteRequestDTO } from './dto/delete-request/delete-request.dto';
import { ForumDeleteRequestDTOResponse } from './dto/forum/forum-delete-request-dto-response.interface';
import { Injectable } from '@angular/core';

@Injectable({providedIn: 'root'})
export class ForumService {
  private baseUrl : String = "http://localhost:8080/api/forums";
  private userId : number;
  constructor(private http: HttpClient,
              private _authService: AuthServiceService,
              private _forumStateService: ForumStateService
          ) {
                this.userId = this._authService.getUserId();
               }

  public getForums() : Observable<ForumDTO[]> {
    return this.http.get<ForumDTO[]>(`${this.baseUrl}`);
  }

  public getForumById(id: string) : Observable<ForumApiResponseDTO> {
    return this.http.get<ForumApiResponseDTO>(`${this.baseUrl}/${id}?userId=${this.userId}`);
  }

  public getForumDataSidebarById(id: string) : Observable<ForumDataSidebar> {
    return this.http.get<ForumDataSidebar>(`${this.baseUrl}/sidebarData/${id}`);
  }

  public getForumsByTitle(title: string, page: number = 0, size: number = 10) : Observable<ForumDTO[]> {
    return this.http.get<ForumDTO[]>(`${this.baseUrl}/search?title=${title}&page=${page}&size=${size}`);
  }

  public forumExists(forumId: number): Observable<ForumExistsDTO> {
    return this.http.get<ForumExistsDTO>(`${this.baseUrl}/exists/${forumId}`);
  }

  public getMultipleForums(ids: number[], page:number = 0, size: number = 10): Observable<ForumDTO[]>{
    let params: HttpParams = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    ids.forEach(id => {
      params = params.append("ids", id.toString());
    })

    return this.http.get<ForumDTO[]>(`${this.baseUrl}/multiple-forums`, { params });
  }

  public isForumCreator(forumId: number): Observable<IsForumCreatorDTO> {
    return this.http.get<IsForumCreatorDTO>(`${this.baseUrl}/isCreator/${forumId}?userId=${this.userId}`);
  }

  public addMember(forumId: number, memberId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${forumId}/members/${memberId}`, {}).pipe(
      tap(() => {
        this.updateLocalStorage(forumId, false);
        this.getForumDataSidebarById(forumId.toString()).subscribe((forum) => {
          this._forumStateService.setForumCreationEvent(forum);
        });
      })
    );
  }

  public removeMember(forumId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${forumId}/members/${this.userId}`).pipe(
      tap(() => {
        this.updateLocalStorage(forumId, false);
        this._forumStateService.setForumRemovalEvent(forumId);
      })
    );
  }

  public saveForum(forum: ForumRequestDTO, creatorId: number) : Observable<ForumDTO> {
    return this.http.post<ForumDTO>(`${this.baseUrl}?creatorId=${creatorId}`, forum);
  }

  public updateForum(forumId: number, forumUpdate: ForumRequestDTO): Observable<void>{
    return this.http.put<void>(`${this.baseUrl}/${forumId}`, forumUpdate);
  }

  public deleteForum(forumId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${forumId}?userId=${this.userId}`).pipe(
      tap(() => {
        this.updateLocalStorage(forumId, true);
        this._forumStateService.setForumRemovalEvent(forumId);
      })
    );
  }

  public addAdmin(forumId: number, userId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${forumId}/admins/${userId}`, {});
  }

  public removeAdmin(forumId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${forumId}/admins/${userId}`);
  }

  public banUser(forumId: number, bannerId: number, bannedId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${forumId}/ban?bannerId=${bannerId}&bannedId=${bannedId}`, {});
  }

  public reportForum(forumId: number, deleteRequest: DeleteRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/report/${forumId}`, deleteRequest);
  }

  public getReportedForums(): Observable<ForumDeleteRequestDTOResponse[]> {
    return this.http.get<ForumDeleteRequestDTOResponse[]>(`${this.baseUrl}/reported`);
  }

  private updateLocalStorage(forumId: number, isDeletion: boolean): void {
    let user = JSON.parse(localStorage.getItem("user")!);

    // Remove from subscribed forums
    user.forumsSubscribed = user.forumsSubscribed.filter((id: number) => id !== forumId);

    if (isDeletion) {
      // Remove from created forums
      user.forumsCreated = user.forumsCreated.filter((id: number) => id !== forumId);
    }

    localStorage.setItem("user", JSON.stringify(user));
  }

}
