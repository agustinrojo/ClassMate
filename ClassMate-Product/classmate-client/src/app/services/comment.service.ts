import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentDTORequest } from './dto/comment/comment-request-dto.interface';
import { Observable } from 'rxjs';
import { CommentDTOResponse } from './dto/comment/comment-response-dto.interface';

@Injectable({providedIn: 'root'})
export class CommentService {
  private baseUrl: string = "http://localhost:8080/api/comments"
  constructor(private http: HttpClient) { }

  public saveComment(comment: CommentDTORequest): Observable<CommentDTORequest>{
    return this.http.post<CommentDTOResponse>(`${this.baseUrl}`, comment);
  }

  public deleteComment(commentId: number, userId: number): Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${commentId}?userId=${userId}`)
  }

}
