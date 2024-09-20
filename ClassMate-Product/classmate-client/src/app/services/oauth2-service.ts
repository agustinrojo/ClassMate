import { Injectable } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../auth/dto/user-dto.interface';
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})
export class OAuth2Service {

  private baseURL: string = "http://localhost:8080/oauth2";
  private userId!: number;

  constructor(private _authService: AuthServiceService,
              private http: HttpClient
  ) {
    this.userId = this._authService.getUserId();
  }

  public connectToGoogle(){
    let user: User = this._authService.getUser();
    let isSynced: boolean = user.synced;
    console.log(isSynced);

    window.location.href = `http://localhost:8085/oauth2/connect/google?userId=${this.userId}&isSynced=${isSynced}`;
  }

  public unsycronize():Observable<void>{
    return this.http.delete<void>(`${this.baseURL}/unsyncronize/${this.userId}`);
  }
}
