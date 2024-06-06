import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SingleForumPageComponent } from './single-forum-page/single-forum-page.component';
import { PostComponent } from './post/post.component';




@NgModule({
  declarations: [
    SingleForumPageComponent,
    PostComponent
  ],
  imports: [
    CommonModule,
  ],
  exports: [
    PostComponent
  ]
})
export class SingleForumPageModule { }
