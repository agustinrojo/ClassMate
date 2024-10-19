import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserProfileWithRoleDTO } from './dto/forum/user/user-profile-with-role-dto.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl: string = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getUsersByForum(forumId: number): Observable<UserProfileWithRoleDTO[]> {
    return this.http.get<UserProfileWithRoleDTO[]>(`${this.baseUrl}/${forumId}`);
  }

}
