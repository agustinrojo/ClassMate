import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostContainerComponent } from './post-container/post-container.component';
import { PostPageComponent } from './post-page/post-page/post-page.component';
import { HomeComponent } from './home/home.component';
import { ForumsPageComponent } from './forums-page/forums-page/forums-page.component';
import { SingleForumPageComponent } from './single-forum-page/single-forum-page/single-forum-page.component';
import { CreatePostComponent } from './single-forum-page/create-post/create-post.component';

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
        path: "forums",
        component: ForumsPageComponent
      },
      {
        path: "forum/:id",
        component: SingleForumPageComponent
      },
      {
        path: "forum/:id/create-post",
        component: CreatePostComponent
      }
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
