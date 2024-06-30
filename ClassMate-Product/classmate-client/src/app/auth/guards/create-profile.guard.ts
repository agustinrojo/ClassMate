import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';

import { Observable, catchError, map, of } from 'rxjs';
import { UserProfileService } from '../../services/user-profile.service';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';

export const CanActivateCreateProfileGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const userId: string = route.paramMap.get("id")!;

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


function checkProfileSet( userId: string ): Observable<boolean>{
  let userProfileService: UserProfileService = inject(UserProfileService);
  let router : Router = inject(Router);

  return userProfileService.getUserProfile(userId).pipe(
    map((resp: UserProfileResponseDTO) => {
      if(resp){
        return false;
      } else {

        return true;
      }
    }),
    catchError(err => {
      console.log(err);
      return of(false);
    })
  )
}
