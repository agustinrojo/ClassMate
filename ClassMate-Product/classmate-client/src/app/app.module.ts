import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BaseChartDirective } from 'ng2-charts';

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
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { StatisticsComponent } from './statistics/statistics.component';
import { ForumActivityChartComponent } from './forum-activity-chart/forum-activity-chart.component';
import { ForumCreationChartComponent } from './app/forum-creation-chart/forum-creation-chart.component';
import { UsersActivityStatsComponent } from './users-activity-stats/users-activity-stats.component';



@NgModule({
  declarations: [
    AppComponent,
    StatisticsComponent,
    ForumActivityChartComponent,
    ForumCreationChartComponent,
    UsersActivityStatsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule,
    RouterModule,
    HomeModule,
    CreateProfileModule,
    BaseChartDirective
  ],
  providers: [
    AuthServiceService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    },
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
