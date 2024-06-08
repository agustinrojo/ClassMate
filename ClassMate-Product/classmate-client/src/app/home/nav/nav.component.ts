import { Component, OnInit } from '@angular/core';
import { AuthServiceService } from '../../auth/auth-service.service';
import { Router } from '@angular/router';
import { User } from '../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit{
  public username!: String;

  constructor(private _authService: AuthServiceService, private _router: Router){}
  ngOnInit(): void {
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.username = user.firstName;
  }

  public logout(){
    this._router.navigate(["login"]);
    this._authService.logout();
  }

  public navigateToForums(){
    console.log("click")
    this._router.navigate(["forums"]);
  }

  public navigateToMain(){
    console.log("click")
    this._router.navigate(["home/main"]);
  }
}
