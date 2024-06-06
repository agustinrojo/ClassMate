import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    PostService
  ]
})
export class PostPageModule { }
