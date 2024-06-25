import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostContainerComponent } from './post-container/post-container.component';
import { PostPageComponent } from './post-page/post-page/post-page.component';
import { HomeComponent } from './home/home.component';
import { ForumsPageComponent } from './forums-page/forums-page/forums-page.component';
import { SingleForumPageComponent } from './single-forum-page/single-forum-page/single-forum-page.component';
import { CreatePostComponent } from './single-forum-page/create-post/create-post.component';
import { CreateForumComponent } from './forums-page/create-forum/create-forum.component';
import { EditPostComponent } from './single-forum-page/edit-post/edit-post.component';
import { CanActivateEditPost, CanMatchEditPost } from '../auth/guards/edit-post.guard';
import { CanActivateForum, CanMatchForum } from '../auth/guards/forum.guard';

export const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
    children: [
      {
        path: "main",
        component: PostContainerComponent,
        pathMatch: "full"
      },
      {
        path: "post/:id",
        component: PostPageComponent
      },
      {
        path: "post/edit/:id",
        component: EditPostComponent,
        canMatch: [CanMatchEditPost],
        canActivate: [CanActivateEditPost]
      },
      {
        path: "forums",
        component: ForumsPageComponent
      },
      {
        path: "forum/:id",
        component: SingleForumPageComponent,
        canMatch: [CanMatchForum],
        canActivate: [CanActivateForum]
      },
      {
        path: "forum/:id/create-post",
        component: CreatePostComponent
      },
      {
        path: "create-forum",
        component: CreateForumComponent
      }
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
