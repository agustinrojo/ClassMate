import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { NavComponent } from './nav/nav.component';
import { PostContainerComponent } from './post-container/post-container.component';

import { SidebarComponent } from './sidebar/sidebar.component';
import { Route, RouterModule, Routes } from '@angular/router';
import { PostPageComponent } from './post-page/post-page/post-page.component';
import { HomeRoutingModule } from './home-routing.module';
import { ForumsPageModule } from './forums-page/forums-page.module';
import { SingleForumPageModule } from './single-forum-page/single-forum-page.module';
import { PostPageModule } from './post-page/post-page.module';
import { FileBadgeComponent } from './file-badge/file-badge.component';



@NgModule({
  declarations: [
    HomeComponent,
    NavComponent,
    PostContainerComponent,
    SidebarComponent

  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    ForumsPageModule,
    SingleForumPageModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
