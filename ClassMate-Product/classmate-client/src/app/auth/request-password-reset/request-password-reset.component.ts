import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthServiceService } from '../auth-service.service';
import { Router } from '@angular/router';
import { validateEmail } from '../validators/validateEmail.validator';

@Component({
  selector: 'app-request-password-reset',
  templateUrl: './request-password-reset.component.html',
  styleUrl: './request-password-reset.component.css'
})
export class RequestPasswordResetComponent implements OnInit{

  public emailForm!: FormGroup;

  constructor(private _fb: FormBuilder, private _authService: AuthServiceService, private _router: Router){

  }

  ngOnInit(): void {
    this.emailForm = this._fb.group({
      email: ['', [Validators.required, Validators.email, validateEmail], []]
    })
    console.log("hola")
    console.log(this.emailForm)
  }

  public onSubmit(){
    this._authService.requestPasswordReset(this.emailForm.value.email).subscribe((resp) => {
      console.log(resp);
      this._router.navigate(["email-sent", {email: this.emailForm.value.email}])
    },
    (err) => {
      console.log(err);
    })
  }

}
