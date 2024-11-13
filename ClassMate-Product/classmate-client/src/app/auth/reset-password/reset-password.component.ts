import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from '../auth-service.service';
import { validatePasswordsMatch } from '../validators/validatePasswordsMatch.validator';
import { ResetPasswordDTO } from '../dto/reset-password-dto.interface';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit{

  public passwordForm!: FormGroup;
  public email?: string;
  public token?: string;
  public passwordResetSuccess: boolean = false;
  public showErr: boolean = false;

  constructor(private _fb: FormBuilder, private _router: Router, private _authService: AuthServiceService, private _route: ActivatedRoute){

  }

  ngOnInit(): void {
    this.getParams();

    this.passwordForm = this._fb.group({
      password: ["", [Validators.required]],
      rePassword: ["", [Validators.required]]
    }, {
      validator: validatePasswordsMatch
    })

  }

  public getParams(){
    this.token = this._route.snapshot.queryParamMap.get('token') || undefined;
    this.email = this._route.snapshot.queryParamMap.get('email') || undefined;
  }

  public onSubmit(){
    const resetPasswordDTO: ResetPasswordDTO = {
      email: this.email!,
      newPassword : this.passwordForm.value.password,
      token: this.token!
    }

    this._authService.resetPassword(resetPasswordDTO).subscribe((resp) => {
      this.passwordResetSuccess = true;
    },
    (err) => {
      this.showErr = true;
    }
    )
  }




}
