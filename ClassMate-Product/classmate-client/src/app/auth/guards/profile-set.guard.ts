import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { UserProfileService } from '../../services/user-profile.service';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';
import { Observable, catchError, map, of } from 'rxjs';
import { AuthServiceService } from '../auth-service.service';

export const CanActivateProfileSetGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {

    return checkProfileSet();
}


export const CanMatchProfileSetGuard: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  return checkProfileSet();
}


export function checkProfileSet(): Observable<boolean>{
  let userProfileService: UserProfileService = inject(UserProfileService);
  let authService: AuthServiceService = inject(AuthServiceService);
  let router : Router = inject(Router);

  return userProfileService.getUserProfile(authService.getUserId().toString()).pipe(
    map((resp: UserProfileResponseDTO) => {
      if(resp){
        return true;
      } else {
        router.navigate(["create-profile"]);
        return false;
      }
    }),
    catchError(err => {
      console.log("falla")
      console.log(err);
      router.navigate(["create-profile"]);
      return of(false);
    })
  )
}
