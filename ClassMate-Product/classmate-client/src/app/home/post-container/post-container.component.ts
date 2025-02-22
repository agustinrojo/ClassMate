import { Component, Input, OnInit } from '@angular/core';
import { PostResponseDTO } from '../../services/dto/post/post-response-dto.interface';
import { AuthServiceService } from '../../auth/auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumApiResponseDTO } from '../../services/dto/forum/forum-api-response-dto.interface';
import { ForumDataSidebar } from '../../services/dto/forum/forum-data-dto.interface';
import { PostService } from '../../services/post.service';
import { SingleForumPageModule } from "../single-forum-page/single-forum-page.module";

@Component({
  selector: 'app-post-container',
  templateUrl: './post-container.component.html',
  styleUrl: './post-container.component.css',

})
export class PostContainerComponent implements OnInit{
  public forums: ForumDataSidebar[] = [];
  public posts: PostResponseDTO[] = [];
  public noPosts: boolean = false;


  constructor(private _authService: AuthServiceService,
              private _forumsService: ForumService,
              private _postService: PostService
  ){ }

  ngOnInit(): void {
    this.loadSubscribedPosts();
    this.noPosts = this.forums.length == 0;
  }

  private loadSubscribedPosts(): void {
    const forumsSubscribed: number[] = this._authService.getForumsSubscribed();
    for (const forumId of forumsSubscribed) {
      this._forumsService.getForumById(forumId.toString()).subscribe((forum: ForumApiResponseDTO) => {
        const forumData: ForumDataSidebar = {
          id: forum.forum.id,
          title: forum.forum.title
        }
        this.forums.unshift(forumData);
        this.posts.push(...forum.posts);
      },
    err => {
      console.log(err)
    })
    }
    if(this.posts.length == 0){
      this.noPosts = true;
    }
    console.log(this.posts.length)
  }

  public deletePost(post: PostResponseDTO) {
    let postId: number = post.id;
    this._postService.deletePost(postId).subscribe(() => {
      let deletedPostIndex:number = this.posts.findIndex(p => p.id == post.id);
      if(deletedPostIndex != -1){
        this.posts.splice(deletedPostIndex, 1);
      }
    })

  }

}
