import { CommonModule } from '@angular/common';
import { SingleForumPageComponent } from './single-forum-page/single-forum-page.component';
import { PostComponent } from './post/post.component';
import { CreatePostComponent } from './create-post/create-post.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditPostComponent } from './edit-post/edit-post.component';
import { PostPageModule } from '../post-page/post-page.module';
import { SharedModule } from '../../shared/shared.module';
import { EditForumComponent } from './edit-forum/edit-forum.component';
import { ForumUsersComponent } from './forum-users/forum-users.component';
import { UserItemComponent } from './forum-users/user-item/user-item.component';
import { NgModule } from '@angular/core';
import { AdminModule } from '../admin/admin.module';




@NgModule({
  declarations: [
    SingleForumPageComponent,
    PostComponent,
    CreatePostComponent,
    EditPostComponent,
    EditForumComponent,
    ForumUsersComponent,
    UserItemComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule
  ],
  exports: [
    PostComponent
  ]
})
export class SingleForumPageModule { }
