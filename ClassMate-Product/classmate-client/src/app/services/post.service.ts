import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostAPIResponseDTO } from './dto/post/post-api-response-dto';
import { PostRequestDTO } from './dto/post/post-request-dto.interface';
import { User } from '../auth/dto/user-dto.interface';
import { PostUpdateDTO } from './dto/post/post-update-dto.interface';
import { IsPostAuthor } from './dto/post/is-post-author-dto.interface';
import { AuthServiceService } from '../auth/auth-service.service';
import { PostResponseDTO } from './dto/post/post-response-dto.interface';

@Injectable({providedIn: 'root'})
export class PostService {

  private baseUrl:string = "http://localhost:8080/api/posts"
  private userId: number;
  constructor(private http: HttpClient,
              private _authService: AuthServiceService
  ) {
    this.userId = this._authService.getUserId();
  }

  public getPostById(id: string) : Observable<PostAPIResponseDTO> {
    return this.http.get<PostAPIResponseDTO>(`${this.baseUrl}/${id}?userId=${this.userId}`);
  }

  public getPostsByName(name: string, page: number = 0, size: number = 10) : Observable<PostResponseDTO[]> {
    return this.http.get<PostResponseDTO[]>(`${this.baseUrl}/search?name=${name}&userId=${this.userId}&page=${page}&size=${size}`);
  }

  public getPostsByNameAndForumId(name: string, forumId: number, page: number = 0, size: number = 10) : Observable<PostResponseDTO[]> {
    return this.http.get<PostResponseDTO[]>(`${this.baseUrl}/search/${forumId}?name=${name}&userId=${this.userId}&page=${page}&size=${size}`);
  }

  public isPostAuthor(postId: number, userId: number): Observable<IsPostAuthor>{
    return this.http.get<IsPostAuthor>(`${this.baseUrl}/isAuthor/${postId}/${userId}`);
  }

  public savePost(post:PostRequestDTO): Observable<PostRequestDTO>{
    let postFormData: FormData = this.mapRequestToFormData(post);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.post<PostRequestDTO>(`${this.baseUrl}`, postFormData, { headers });
  }

  public upvotePost(postId: number): Observable<void>{
    return this.http.post<void>(`${this.baseUrl}/${postId}/upvote?userId=${this.userId}`, {})
  }

  public downvotePost(postId: number): Observable<void>{
    return this.http.post<void>(`${this.baseUrl}/${postId}/downvote?userId=${this.userId}`, {})
  }

  public removePostVote(postId: number): Observable<void>{
    return this.http.post<void>(`${this.baseUrl}/${postId}/removeVote?userId=${this.userId}`, {})
  }
  public deletePost(postId: number): Observable<void>{
    let user: User = JSON.parse(localStorage.getItem("user")!);
    let userId = user.id;
    return this.http.delete<void>(`${this.baseUrl}/${postId}?userId=${userId}`)
  }

  public updatePost(postId: number, updatedPost: PostUpdateDTO) : Observable<void>{
    let updateFormData: FormData = this.mapUpdateRequestToFormData(updatedPost);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.put<void>(`${this.baseUrl}/${postId}`, updateFormData, { headers });
  }

  public mapRequestToFormData(req: PostRequestDTO): FormData {
    const formData = new FormData();
    formData.append("id", req.id.toString());
    formData.append("forumId", req.forumId.toString());
    formData.append("authorId", req.authorId.toString());
    formData.append("title", req.title.toString());
    formData.append("body", req.body.toString());

    if (req.files && req.files.length > 0) {
      for (let i = 0; i < req.files.length; i++) {
        formData.append('files', req.files[i]);
      }
    }

    return formData;
  }

  public mapUpdateRequestToFormData(req: PostUpdateDTO): FormData{
    const formData = new FormData();
    formData.append("title", req.title.toString());
    formData.append("body", req.body.toString());
    if(req.fileIdsToRemove.length > 0){
      for (let i = 0; i < req.fileIdsToRemove.length; i++) {
        formData.append('fileIdsToRemove', req.fileIdsToRemove[i].toString());
      }
    }

    if(req.filesToAdd.length > 0){
      for (let i = 0; i < req.filesToAdd.length; i++) {
        formData.append('filesToAdd', req.filesToAdd[i]);
      }
    }

    return formData;
  }

}
