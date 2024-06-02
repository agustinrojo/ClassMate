import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmailSentComponent } from './auth/email-sent/email-sent.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { MainComponent } from './main/main.component';
import { canActivateGuard, canMatchGuard } from './auth/guards/auth.guard';

const routes: Routes = [
  {
    path     : "email-sent",
    component: EmailSentComponent
  },
  {
    path     : "register",
    component: RegisterComponent
  },
  {
    path     : "",
    component: LoginComponent
  },
  {
    path     : "login",
    component: LoginComponent
  },
  {
    path        : "main",
    component   :  MainComponent,
    canActivate : [canActivateGuard],
    canMatch    : [canMatchGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
