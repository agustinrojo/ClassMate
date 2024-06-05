import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { NavComponent } from './nav/nav.component';
import { PostContainerComponent } from './post-container/post-container.component';
import { PostComponent } from './post/post.component';
import { SidebarComponent } from './sidebar/sidebar.component';



@NgModule({
  declarations: [
    HomeComponent,
    NavComponent,
    PostContainerComponent,
    PostComponent,
    SidebarComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
