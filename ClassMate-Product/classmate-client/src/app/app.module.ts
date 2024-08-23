import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { CapitalizePipe } from './shared/pipes/capitalize.pipe';
import { AuthServiceService } from './auth/auth-service.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { HttpRequestInterceptor } from './interceptor/http.interceptor';
import { HomeModule } from './home/home.module';
import { TruncatePipe } from './shared/pipes/truncate.pipe';
import { FileSizePipe } from './shared/pipes/file-size.pipe';
import { CreateProfileComponent } from './create-profile/create-profile.component';
import { CreateProfileModule } from './create-profile/create-profile.module';



@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule,
    RouterModule,
    HomeModule,
    CreateProfileModule,

  ],
  providers: [
    AuthServiceService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
