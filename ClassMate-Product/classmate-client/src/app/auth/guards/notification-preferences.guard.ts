import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { AuthServiceService } from '../auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumExistsDTO } from '../../services/dto/forum/forum-exists-dto.interface';

export const CanActivateNotificationPreferences: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const userId: number = parseInt(route.paramMap.get("id")!);
    return checkUser(userId);
}


export const CanMatchNotificationPreferences: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const idSegment = segments.find(segment => segment.path.match(/^\d+$/));
  const userId = idSegment ? parseInt(idSegment.path) : 0;
  return checkUser(userId);
}


function checkUser(userId: number): Observable<boolean>{
  const _authService: AuthServiceService = inject(AuthServiceService);
  const loggedUserId:number = _authService.getUserId();
  const _router: Router = inject(Router);
  const userCheck: boolean = (userId == loggedUserId);

  if(!userCheck){
    _router.navigate([`profile/${userId}`])
  }
  return of(userCheck);

}
