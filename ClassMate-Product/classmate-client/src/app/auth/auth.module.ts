import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CapitalizePipe } from '../shared/pipes/capitalize.pipe';
import { ReactiveFormsModule } from '@angular/forms';
import { EmailSentComponent } from './email-sent/email-sent.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    EmailSentComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    SharedModule

  ],
  exports: [
    LoginComponent,
    RegisterComponent,
    EmailSentComponent
  ]
})
export class AuthModule { }
