import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ForumDTO } from './dto/forum/forum-dto.interface';
import { ForumApiResponseDTO } from './dto/forum/forum-api-response-dto.interface';

@Injectable({providedIn: 'root'})
export class ForumService {
  private baseUrl : String = "http://localhost:8080/api/forums";
  constructor(private http: HttpClient) { }

  public getForums() : Observable<ForumDTO[]> {
    return this.http.get<ForumDTO[]>(`${this.baseUrl}`);
  }

  public getForumById(id: string) : Observable<ForumApiResponseDTO> {
    return this.http.get<ForumApiResponseDTO>(`${this.baseUrl}/${id}`);
  }

}
