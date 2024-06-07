import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    PostService
  ]
})
export class PostPageModule { }
