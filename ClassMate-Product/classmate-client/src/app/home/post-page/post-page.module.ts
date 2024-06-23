import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditCommentComponent } from './edit-comment/edit-comment.component';
import { FileBadgeComponent } from '../../shared/file-badge/file-badge.component';
import { HomeModule } from '../home.module';
import { TruncatePipe } from '../../shared/pipes/truncate.pipe';
import { FileSizePipe } from '../../shared/pipes/file-size.pipe';
import { SharedModule } from '../../shared/shared.module';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent,
    EditCommentComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule
  ],
  providers: [
    PostService
  ]
})
export class PostPageModule { }
