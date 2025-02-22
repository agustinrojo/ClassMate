import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';

import { Observable, catchError, map, of } from 'rxjs';
import { UserProfileService } from '../../services/user-profile.service';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';
import { AuthServiceService } from '../auth-service.service';
import { inject } from '@angular/core';

export const CanActivateCreateProfileGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const authService: AuthServiceService = inject(AuthServiceService);
    const userId: string = authService.getUserId().toString();

    return checkProfileSet(userId);
}


export const CanMatchCreateProfileGuard: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const idSegment = segments.find(segment => segment.path.match(/^\d+$/));
  const userId = idSegment ? idSegment.path : "0";
  return checkProfileSet(userId);
}


export function checkProfileSet( userId: string ): Observable<boolean>{
  let userProfileService: UserProfileService = inject(UserProfileService);
  let router : Router = inject(Router);

  return userProfileService.getUserProfile(userId).pipe(
    map((resp: UserProfileResponseDTO) => {
      if(resp){
        router.navigate(["home/main"]);
        return false;
      } else {
        return true;
      }
    }),
    catchError(err => {
      console.log("falla")
      console.log(err);
      return of(true);
    })
  )
}
