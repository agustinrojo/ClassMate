import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginRequest } from '../dto/login-request.interface';
import { AuthServiceService } from '../auth-service.service';
import { LoginResponse } from '../dto/login-response.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

  public btnDisabled: boolean;
  public loading:boolean;
  public credencialesIncorrectas: boolean;
  public loginForm!: FormGroup;

constructor(private _fb: FormBuilder, private _authService: AuthServiceService, private _router: Router){
  this.btnDisabled = false;
  this.loading = false;
  this.credencialesIncorrectas = false;
}



  ngOnInit(): void {
    this.loginForm = this._fb.group({
      email   : ["", [Validators.required, Validators.email] ],
      password: ["", Validators.required ]
    })
  }

  public onSubmit(){
    this.btnDisabled = true;
    this.loading = true;
    let req:LoginRequest = {...this.loginForm.value};
    this.login(req);

  }

  private login(req:LoginRequest){
    this._authService.authenticate(req)
      .subscribe((resp: LoginResponse) => {
        this.loginSuccess(resp);
      },
      (err) => {
        this.loginFail(err);
      })
  }

  private loginSuccess(resp: LoginResponse){
    this._authService.setAuthData(resp);
    this._router.navigate(["home/main"]);
    this.loading = false;
    this.credencialesIncorrectas = false;
  }

  private loginFail(err: any){
    this.loading = false;
        this.btnDisabled = false
        this.credencialesIncorrectas = true;
        this.loginForm.reset();
        console.log(err);
  }

}
