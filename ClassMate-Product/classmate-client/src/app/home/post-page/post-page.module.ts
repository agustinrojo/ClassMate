import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditCommentComponent } from './edit-comment/edit-comment.component';
import { FileBadgeComponent } from '../file-badge/file-badge.component';
import { HomeModule } from '../home.module';
import { TruncatePipe } from '../../pipes/truncate.pipe';
import { FileSizePipe } from '../../pipes/file-size.pipe';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent,
    EditCommentComponent,
    FileBadgeComponent,
    TruncatePipe,
    FileSizePipe
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
