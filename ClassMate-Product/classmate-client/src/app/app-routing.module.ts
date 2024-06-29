import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmailSentComponent } from './auth/email-sent/email-sent.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { canActivateGuard, canMatchGuard } from './auth/guards/auth.guard';
import { HomeComponent } from './home/home/home.component';
import { CreateProfileComponent } from './create-profile/create-profile.component';
import { CanActivateProfileSetGuard, CanMatchProfileSetGuard } from './auth/guards/profile-set.guard';
import { CanActivateCreateProfileGuard, CanMatchCreateProfileGuard } from './auth/guards/create-profile.guard';

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
    path       : "create-profile",
    component  : CreateProfileComponent,
    // canMatch   : [CanMatchCreateProfileGuard],
    // canActivate: [CanActivateCreateProfileGuard]
  },
  {
    path       : "home",
    loadChildren: () => import('./home/home.module').then( m => m.HomeModule ),
    canMatch   : [canMatchGuard, CanMatchProfileSetGuard],
    canActivate: [canActivateGuard, CanActivateProfileSetGuard]
  },
  {
    path       : "post/**",
    component  : HomeComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
