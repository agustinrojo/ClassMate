import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmailSentComponent } from './auth/email-sent/email-sent.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { canActivateGuard, canMatchGuard } from './auth/guards/auth.guard';
import { HomeComponent } from './home/home/home.component';

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
    path       : "home",
    loadChildren: () => import('./home/home.module').then( m => m.HomeModule ),
    canMatch   : [canMatchGuard],
    canActivate: [canActivateGuard]
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
