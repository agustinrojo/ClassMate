import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthServiceService } from '../auth/auth-service.service';
import { Router } from '@angular/router';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(req.url != "http://localhost:8080/api/auth/register" && req.url != "http://localhost:8080/api/auth/authenticate" && req.url != "http://localhost:8080/api/auth/validate" && req.url != "http://localhost:8080/api/auth/refresh_token"){

      const modifiedReq = req.clone({
        headers: req.headers.set("Authorization", `Bearer ${localStorage.getItem("accessToken")}`)
      })
      return next.handle(modifiedReq);
    }

    return next.handle(req);
  }
}
