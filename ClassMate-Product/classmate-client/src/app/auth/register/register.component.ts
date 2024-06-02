import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CapitalizePipe } from '../../pipes/capitalize.pipe';
import { validateEmail } from '../validators/validateEmail.validator';
import { validatePasswordsMatch } from '../validators/validatePasswordsMatch.validator';
;
import { AuthServiceService } from '../auth-service.service';
import { RegisterRequest } from '../dto/register-request.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{
  public registerForm!: FormGroup;
  public carreras     : string[];
  public loading      : boolean;


  constructor(private _fb: FormBuilder, private _authService: AuthServiceService, private _router:Router){
    this.carreras = ["carrera", "sistemas", "industrial", "mecanica", "quimica", "electrica", "electronica"];
    this.loading  = false;
  }
  ngOnInit(): void {
    this.registerForm = this._fb.group({
      firstName : ["", Validators.required],
      lastName  : ["", Validators.required],
      legajo    : ["", Validators.required],
      carrera   : ["", Validators.required],
      email     : ["", [Validators.required, Validators.email, validateEmail]],
      password  : ["", Validators.required],
      rePassword: ["", Validators.required]
    }, {
      validator: validatePasswordsMatch
    })


  }

  public onSubmit(){

    this.loading = true;

    let req: RegisterRequest = {
      firstName: this.registerForm.get("firstName")?.value,
      lastName : this.registerForm.get("lastName")?.value,
      legajo   : this.registerForm.get("legajo")?.value,
      carrera  : this.registerForm.get("carrera")?.value,
      email    : this.registerForm.get("email")?.value,
      password : this.registerForm.get("password")?.value
    };

    this._authService.register(req)
    .subscribe(resp => {
      this.loading = false;
      this._router.navigate(["email-sent", {email: req.email}])
    },
    err => {
      this.loading = false;
      console.log(err);
    })



  }



}
