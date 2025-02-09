import { ActivatedRouteSnapshot, CanActivateFn, CanMatchFn, Route, Router, RouterStateSnapshot, UrlSegment } from "@angular/router";
import { AuthServiceService } from "../auth-service.service";
import { inject } from "@angular/core";


export const CanActivateAdminGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
  ) => {
    const authService: AuthServiceService = inject(AuthServiceService);
    let router : Router = inject(Router);

    if(authService.getUser().role.toString() == "ADMIN"){
      return true
    } else {
      router.navigate(["home/main"])
      return false;
    }

}


export const CanMatchAdminGuard: CanMatchFn = (
  route: Route,
  segments: UrlSegment[]
) => {
  const authService: AuthServiceService = inject(AuthServiceService);
  let router : Router = inject(Router);

  if(authService.getUser().role.toString() == "ADMIN"){
    return true
  } else {
    router.navigate(["home/main"])
    return false;
  }
}
