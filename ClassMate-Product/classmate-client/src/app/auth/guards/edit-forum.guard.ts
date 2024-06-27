import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { ForumService } from '../../services/forum.service';
import { Observable, catchError, map, of } from 'rxjs';
import { IsForumCreatorDTO } from '../../services/dto/forum/is-forum-creator-dto.interface';

export const CanActivateEditForum: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const forumId: number = parseInt(route.paramMap.get("id")!);
    return checkForumAuthor(forumId);
}


export const CanMatchEditForum: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const idSegment = segments.find(segment => segment.path.match(/^\d+$/));
  const forumId = idSegment ? parseInt(idSegment.path) : 0;

  return checkForumAuthor(forumId);
}

function checkForumAuthor(forumId: number): Observable<boolean>{
  let _forumService: ForumService = inject(ForumService);
  let _router: Router = inject(Router);

  return _forumService.isForumCreator(forumId).pipe(
    map((resp: IsForumCreatorDTO) => {
      if(resp.author){
        return true;
      } else {
        _router.navigate(["home/main"]);
        return false;
      }
    }),
    catchError(() => {
      _router.navigate(["home/main"]);
      return of(false);
    })
  )
}
