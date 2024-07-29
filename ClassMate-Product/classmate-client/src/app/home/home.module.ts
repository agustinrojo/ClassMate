import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { NavComponent } from './nav/nav.component';
import { PostContainerComponent } from './post-container/post-container.component';

import { Route, RouterModule, Routes } from '@angular/router';
import { PostPageComponent } from './post-page/post-page/post-page.component';
import { HomeRoutingModule } from './home-routing.module';
import { ForumsPageModule } from './forums-page/forums-page.module';
import { SingleForumPageModule } from './single-forum-page/single-forum-page.module';
import { PostPageModule } from './post-page/post-page.module';
import { FileBadgeComponent } from '../shared/file-badge/file-badge.component';
import { ProfilePageComponent } from './profile-page/profile-page/profile-page.component';
import { EditProfileComponent } from './profile-page/edit-profile/edit-profile.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { ForumSearchResultComponent } from './search/forum-search-result/forum-search-result.component';
import { PostSearchResultComponent } from './search/post-search-result/post-search-result.component';



@NgModule({
  declarations: [
    HomeComponent,
    NavComponent,
    PostContainerComponent,
    ProfilePageComponent,
    EditProfileComponent,
    ForumSearchResultComponent,
    PostSearchResultComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    ForumsPageModule,
    SingleForumPageModule,
    ReactiveFormsModule,
    SharedModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
