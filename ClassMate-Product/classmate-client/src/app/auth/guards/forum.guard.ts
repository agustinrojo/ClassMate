import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { AuthServiceService } from '../auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumExistsDTO } from '../../services/dto/forum/forum-exists-dto.interface';

export const CanActivateForum: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const forumId: number = parseInt(route.paramMap.get("id")!);
    return checkForum(forumId);
}


export const CanMatchForum: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const idSegment = segments.find(segment => segment.path.match(/^\d+$/));
  const forumId = idSegment ? parseInt(idSegment.path) : 0;
  return checkForum(forumId);
}


function checkForum(forumId: number): Observable<boolean> {
  let _forumService: ForumService = inject(ForumService);
  let _router : Router = inject(Router);

  return _forumService.forumExists(forumId).pipe(
    map((resp: ForumExistsDTO) => {
      if(resp.exists){
        return true;
      } else {
        _router.navigate(["home/main"]);
        return false;
      }
    }),
    catchError((err) => {
      console.log(err)
      _router.navigate(["home/main"]);
      return of(false);
    })
  )
}
