import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentDTORequest } from './dto/comment/comment-request-dto.interface';
import { Observable } from 'rxjs';
import { CommentDTOResponse } from './dto/comment/comment-response-dto.interface';
import { CommentUpdateDTO } from './dto/comment/comment-update-dto.interface';


@Injectable({providedIn: 'root'})
export class CommentService {
  private baseUrl: string = "http://localhost:8080/api/comments"
  constructor(private http: HttpClient) { }

  public getCommentById(commentId: number): Observable<CommentDTOResponse>{


    return this.http.get<CommentDTOResponse>(`${this.baseUrl}/${commentId}`);
  }

  public saveComment(comment: CommentDTORequest): Observable<CommentDTOResponse>{
    let commentFormData: FormData = this.mapRequestToFormData(comment);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.post<CommentDTOResponse>(`${this.baseUrl}`, commentFormData, { headers });
  }

  public deleteComment(commentId: number, userId: number): Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${commentId}?userId=${userId}`)
  }

  public editComment(commentId: number, commentUpdate: CommentUpdateDTO): Observable<void> {
    let updateFormData : FormData = this.mapUpdateToFormData(commentUpdate);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.put<void>(`${this.baseUrl}/${commentId}`, updateFormData, { headers });
  }

  public mapRequestToFormData(req: CommentDTORequest): FormData {
    const formData = new FormData();
    formData.append("id", req.id.toString())
    formData.append("postId", req.postId.toString())
    formData.append("authorId", req.authorId.toString())
    formData.append("body", req.body.toString())
    //formData.append("files", req.files)
    if (req.files && req.files.length > 0) {
      for (let i = 0; i < req.files.length; i++) {
        formData.append('files', req.files[i]);
      }
    }

    return formData;
  }

  public mapUpdateToFormData(commentUpdate: CommentUpdateDTO): FormData{
    const formData = new FormData();
    formData.append("body", commentUpdate.body);

    if(commentUpdate.fileIdsToRemove.length > 0){
      for (const id of commentUpdate.fileIdsToRemove) {
        formData.append('fileIdsToRemove', id);
      }
    }

    if(commentUpdate.filesToAdd.length > 0){
      for(const file of commentUpdate.filesToAdd){
        formData.append("filesToAdd", file);
      }
    }

    return formData;
  }

}
