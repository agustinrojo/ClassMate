import { Component } from '@angular/core';
import { AuthServiceService } from '../../auth/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {

  constructor(private _authService: AuthServiceService, private _router: Router){}

  public logout(){
    this._router.navigate(["login"]);
    this._authService.logout();
  }
}
