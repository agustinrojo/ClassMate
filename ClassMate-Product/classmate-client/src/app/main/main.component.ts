import { Component } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {

  constructor(private _authService: AuthServiceService,
              private _router: Router
  ){}

  public logout(){
    this._authService.logout();
    this._router.navigate(["login"]);
  }
}
