import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanMatch, GuardResult, MaybeAsync, Route, UrlSegment, RouterStateSnapshot, CanMatchFn, CanActivateFn, Router } from '@angular/router';
import { AuthServiceService } from '../auth-service.service';
import { Observable, catchError, map, of, switchMap } from 'rxjs';
import { ValidationResponse } from '../dto/validation-response.interface';



const checkAuthStatus = (): Observable<boolean> => {
  //se inyectan el AuthService y el Router
  const authService: AuthServiceService = inject(AuthServiceService);
  const router: Router = inject(Router);

  let validated : Observable<boolean> = of(false);
  return authService.checkAuthentication().pipe(
    map((resp: ValidationResponse) => {
      if (resp.valid) {
        return true;
      } else {
        router.navigate(['/login']);
        return false;
      }
    }),
    catchError((err) => {

      router.navigate(['/login']);
      return of(false);
    })
  );
}



export const canMatchGuard: CanMatchFn = (
  //Tipado CanMatchFN
  route: Route,
  segments: UrlSegment[]
) => {


  return checkAuthStatus();
};


export const canActivateGuard: CanActivateFn = (
  //Hay que tener en cuenta el tipado CanActiveFn
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {


  return checkAuthStatus();
};

// function checkIfUserHasProfileSet(){
//   let profileSet: boolean = false;
//   console.log("checkin")
//   checkProfileSet().subscribe((resp: boolean) => {
//     console.log(resp)
//     profileSet = resp;
//   })

//   return profileSet;
// }

