import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostAPIResponseDTO } from './dto/post/post-api-response-dto';
import { PostDTO } from './dto/post/post-dto.interface';

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

}
