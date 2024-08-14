import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot } from '@angular/router';
import { CanMatchFn, Route, Router, UrlSegment } from '@angular/router';
import { AuthServiceService } from '../auth-service.service';
import { PostService } from '../../services/post.service';
import { IsPostAuthor } from '../../services/dto/post/is-post-author-dto.interface';
import { Observable, catchError, map, of } from 'rxjs';


export const CanActivateEditPost: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const postId: number = parseInt(route.paramMap.get("id")!);
    console.log("Post ID in guard:", postId);
    return checkPostAuthor(postId);
}



export const CanMatchEditPost: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const postIdSegment = segments[4];
  const postId = postIdSegment ? parseInt(postIdSegment.path) : 0;

  return checkPostAuthor(postId);
}

function checkPostAuthor(postId: number) : Observable<boolean>{
  let _authService: AuthServiceService = inject(AuthServiceService);
  let _postService: PostService = inject(PostService);
  let _router: Router = inject(Router);


  const userId: number = _authService.getUserId();
  return _postService.isPostAuthor(postId, userId).pipe(
    map((resp: IsPostAuthor) => {
      console.log("Post ID when hitting api:", postId);
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
