import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable, catchError, of, switchMap, tap, throwError } from 'rxjs';
import { RegisterResponse } from './dto/register-response.interface';
import { RegisterRequest } from './dto/register-request.interface';
import { LoginRequest } from './dto/login-request.interface';
import { LoginResponse } from './dto/login-response.interface';
import { User } from './dto/user-dto.interface';
import { ValidationResponse } from './dto/validation-response.interface';
import { ValidationRequest } from './dto/validation-request.interface';
import { UserData } from './interfaces/user-data.interface';
import { ResetPasswordDTO } from './dto/reset-password-dto.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private baseUrl: string = "http://localhost:8080";

  constructor(private http: HttpClient  ) { }

  public register(userData: RegisterRequest): Observable<RegisterResponse>{
    let url: string = `${this.baseUrl}/api/auth/register`
    return this.http.post<RegisterResponse>(url, userData);
  }

  public authenticate(loginData: LoginRequest): Observable<LoginResponse>{
    let url: string = `${this.baseUrl}/api/auth/authenticate`;
    return this.http.post<LoginResponse>(url, loginData);
  }

  public setAuthData(authData:LoginResponse){
    this.setAccessToken(authData.accessToken);
    this.setRefreshToken(authData.refreshToken);
    let user: User = authData.user;
    localStorage.setItem("user", JSON.stringify(user));
  }

  public checkAuthentication(): Observable<ValidationResponse>{
    if ( !localStorage.getItem("accessToken") ) return of({ valid: false });
    if( !localStorage.getItem("user") ) return of({ valid: false });

    const user : User = JSON.parse(localStorage.getItem("user") || "{}");

    const jwt : string = localStorage.getItem("accessToken") || "";
    const id  : number = user.id || 0;

    const req : ValidationRequest = {
      token: jwt,
      userId: id
    }

    let url: string = `${this.baseUrl}/api/auth/validate-user-token`
    return this.http.post<ValidationResponse>(url, req);
  }

  public logout(){
    let url: string = `${this.baseUrl}/api/auth/logout`;
    this.http.post<LoginResponse>(url, {});

    this.clearTokens();
    localStorage.removeItem("user")
  }

  public refreshToken(): Observable<any> {
    const refreshToken = this.getRefreshToken();
    console.log(refreshToken)
    if (!refreshToken) {
      return throwError("No refresh token available");
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${refreshToken}`);
    return this.http.post(`http://localhost:8080/api/auth/refresh_token`, {}, { headers }).pipe(
      tap(response => {console.log(response)}),
      catchError(error => {
        // Handle refresh token request errors
        console.error("Error refreshing token:", error);
        this.clearTokens(); // Clear tokens on error
        return throwError("Token refresh failed");
      })
    );
  }

  public requestPasswordReset(email:string): Observable<any>{
    return this.http.post(`${this.baseUrl}/api/auth/request-reset-password?email=${email}`, {});
  }

  public resetPassword(resetPasswordDTO: ResetPasswordDTO){
    return this.http.post(`${this.baseUrl}/api/auth/reset-password`, resetPasswordDTO);
  }

  public getRefreshToken(): string {
    return localStorage.getItem("refreshToken") || "";
  }

  public setAccessToken(token:string){
    localStorage.setItem("accessToken", token);
  }

  public setRefreshToken(refreshToken: string){
    localStorage.setItem("refreshToken", refreshToken);
  }

  public clearTokens(){
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }

  public getUserId(): number{
    let user: User = JSON.parse(localStorage.getItem("user")!);
    return user.id;
  }

  public getForumsSubscribed(): number[] {
    let user: User = JSON.parse(localStorage.getItem("user")!);
    return user.forumsSubscribed;
  }

  // public getForumsAdminDeprecated(): number[] {
  //   let user: User = JSON.parse(localStorage.getItem("user")!);
  //   return user.forumsAdmin;
  // }

  public getForumsAdmin(): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/api/users/${this.getUserId()}/forumsAdmin`);
  }

  public getUser(): User {
    return JSON.parse(localStorage.getItem("user")!);
  }

  public getAccessToken(): string {
    return (localStorage.getItem("accessToken")!);
  }

  public setSynced(synced: boolean){
    let user: User = this.getUser();
    user.synced = synced;
    localStorage.setItem("user", JSON.stringify(user));
  }

  public getUserData(): UserData {
    let user: User = this.getUser();
    let userFistLastName : UserData = {
      firstName: user.firstName,
      lastName : user.lastName,
      legajo   : user.legajo
    }
    return userFistLastName;
  }

  public setNewChatroomIds(chatroomIds: number[]){
    let user: User = this.getUser();
    user.chatroomIdsIn = chatroomIds;
    localStorage.setItem("user", JSON.stringify(user));
  }
}
