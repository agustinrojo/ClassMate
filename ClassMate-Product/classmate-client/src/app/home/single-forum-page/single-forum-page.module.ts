import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SingleForumPageComponent } from './single-forum-page/single-forum-page.component';
import { PostComponent } from './post/post.component';
import { CreatePostComponent } from './create-post/create-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditPostComponent } from './edit-post/edit-post.component';




@NgModule({
  declarations: [
    SingleForumPageComponent,
    PostComponent,
    CreatePostComponent,
    EditPostComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    PostComponent
  ]
})
export class SingleForumPageModule { }
