import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthServiceService } from '../auth/auth-service.service';
import { Router } from '@angular/router';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  private WHITE_LIST_URL = [
    "http://localhost:8080/api/auth/register",
    "http://localhost:8080/api/auth/authenticate",
    "http://localhost:8080/api/auth/validate",
    "http://localhost:8080/api/auth/refresh_token",
    "http://localhost:8080/api/auth/request-reset-password",
    "http://localhost:8080/api/auth/reset-password"
    ]

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(req.url != "http://localhost:8080/api/auth/register" &&
      req.url != "http://localhost:8080/api/auth/authenticate" &&
      req.url != "http://localhost:8080/api/auth/validate" &&
      req.url != "http://localhost:8080/api/auth/refresh_token" &&
      !req.url.includes("http://localhost:8080/api/auth/request-reset-password") &&
      req.url != "http://localhost:8080/api/auth/reset-password"
      ){

      const modifiedReq = req.clone({
        // headers: req.headers.set("Authorization", `Bearer ${localStorage.getItem("accessToken")}`)
        headers: req.headers.append("Authorization", `Bearer ${localStorage.getItem("accessToken")}`)
      })


      return next.handle(modifiedReq);
    }

    return next.handle(req);
  }
}
