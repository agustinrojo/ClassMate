import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private baseUrl: string = "http://localhost:8080/api/files"

  constructor(private http: HttpClient) { }

  public downloadFile(fileId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${fileId}`, {responseType: 'blob'});
  }

}
