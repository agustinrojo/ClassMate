import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EditCommentComponent } from './edit-comment/edit-comment.component';
import { SharedModule } from '../../shared/shared.module';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent,
    EditCommentComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatMenuModule,
    SharedModule
  ],
  exports: [
    CommentComponent
  ],
  providers: [
    PostService
  ]
})
export class PostPageModule { }
