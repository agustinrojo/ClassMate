import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostPageComponent } from './post-page/post-page.component';
import { PostService } from '../../services/post.service';
import { CommentComponent } from './comment/comment.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EditCommentComponent } from './edit-comment/edit-comment.component';
import { FileBadgeComponent } from '../../shared/file-badge/file-badge.component';
import { HomeModule } from '../home.module';
import { TruncatePipe } from '../../shared/pipes/truncate.pipe';
import { FileSizePipe } from '../../shared/pipes/file-size.pipe';
import { SharedModule } from '../../shared/shared.module';
import { ReportCommentDialogComponent } from './report-comment-dialog/report-comment-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';



@NgModule({
  declarations: [
    PostPageComponent,
    CommentComponent,
    EditCommentComponent,
    ReportCommentDialogComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    MatMenuModule
  ],
  providers: [
    PostService
  ]
})
export class PostPageModule { }
