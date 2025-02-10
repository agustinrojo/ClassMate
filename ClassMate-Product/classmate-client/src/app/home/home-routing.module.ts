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
import { EditForumComponent } from './single-forum-page/edit-forum/edit-forum.component';
import { CanActivateEditForum, CanMatchEditForum } from '../auth/guards/edit-forum.guard';
import { ProfilePageComponent } from './profile-page/profile-page/profile-page.component';
import { EditProfileComponent } from './profile-page/edit-profile/edit-profile.component';
import { ForumSearchResultComponent } from './search/forum-search-result/forum-search-result.component';
import { PostSearchResultComponent } from './search/post-search-result/post-search-result.component';
import { ChatContainerComponent } from './chat/chat-container/chat-container.component';
import { CalendarComponent } from './calendar/calendar/calendar.component';
import { NotificationPreferencesComponent } from './profile-page/notification-preferences/notification-preferences.component';
import { CanActivateNotificationPreferences, CanMatchNotificationPreferences } from '../auth/guards/notification-preferences.guard';
import { UserSearchResultComponent } from './search/user-search-result/user-search-result.component';
import { StatisticsComponent } from '../statistics/statistics.component';


export const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
    children: [
      {
        path: "home/main",
        component: PostContainerComponent,
        pathMatch: "full"
      },
      {
        path: "forum/:forumId/post/:id",
        component: PostPageComponent
      },
      {
        path: "forum/:forumId/post/edit/:id",
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
        path: "forum/edit/:id",
        component: EditForumComponent,
        canMatch: [CanMatchEditForum],
        canActivate: [CanActivateEditForum]
      },
      {
        path: "forum/:id/create-post",
        component: CreatePostComponent
      },
      {
        path: "create-forum",
        component: CreateForumComponent
      },
      {
        path: "profile/:id",
        component: ProfilePageComponent
      },
      {
        path: "profile/:id/edit",
        component: EditProfileComponent
      },
      {
        path: "profile/:id/notification-preferences",
        component: NotificationPreferencesComponent,
        canMatch: [CanMatchNotificationPreferences],
        canActivate: [CanActivateNotificationPreferences]
      },
      {
        path: 'forums/search',
        component: ForumSearchResultComponent // Add forum search results route
      },
      {
        path: 'forum/:forumId/posts/search',
        component: PostSearchResultComponent // Add post search results route
      },
      {
        path: 'posts/search',
        component: PostSearchResultComponent // Add post search results route
      },
      {
        path: 'users/search',
        component: UserSearchResultComponent // Add post search results route
      },
      {
        path       : "chat",
        component  : ChatContainerComponent
      },
      {
        path: "calendar",
        component: CalendarComponent
      },
      {
        path: 'statistics',
        component: StatisticsComponent,
      },
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
