import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentDTO } from './dto/comment/comment-dto.interface';
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})
export class CommentService {
  private baseUrl: string = "http://localhost:8080/api/comments"
  constructor(private http: HttpClient) { }

  public saveComment(comment: CommentDTO): Observable<CommentDTO>{
    return this.http.post<CommentDTO>(`${this.baseUrl}`, comment);
  }

  public deleteComment(commentId: number, userId: number): Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${commentId}?userId=${userId}`)
  }

}
