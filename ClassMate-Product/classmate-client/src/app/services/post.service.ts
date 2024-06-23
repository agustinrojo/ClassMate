import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostAPIResponseDTO } from './dto/post/post-api-response-dto';
import { PostDTO } from './dto/post/post-dto.interface';
import { User } from '../auth/dto/user-dto.interface';
import { PostUpdateDTO } from './dto/post/post-update-dto.interface';

@Injectable({providedIn: 'root'})
export class PostService {

  private baseUrl:string = "http://localhost:8080/api/posts"

  constructor(private http: HttpClient) { }

  public getPostById(id: string) : Observable<PostAPIResponseDTO> {
    return this.http.get<PostAPIResponseDTO>(`${this.baseUrl}/${id}`);
  }

  public savePost(post:PostDTO): Observable<PostDTO>{
    return this.http.post<PostDTO>(`${this.baseUrl}`, post);
  }

  public deletePost(postId: number){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    let userId = user.id;
    return this.http.delete<void>(`${this.baseUrl}/${postId}?userId=${userId}`)
  }

  public updatePost(postId: number, updatedPost: PostUpdateDTO) : Observable<void>{
    return this.http.put<void>(`${this.baseUrl}/${postId}`, updatedPost)
  }

}
