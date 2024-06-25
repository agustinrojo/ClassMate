import { Component, OnInit, Renderer2 } from '@angular/core';
import { ForumService } from '../../../services/forum.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumApiResponseDTO } from '../../../services/dto/forum/forum-api-response-dto.interface';
import { PostService } from '../../../services/post.service';
import { PostRequestDTO } from '../../../services/dto/post/post-request-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';

@Component({
  selector: 'app-single-forum-page',
  templateUrl: './single-forum-page.component.html',
  styleUrl: './single-forum-page.component.css'
})
export class SingleForumPageComponent implements OnInit{
  public userId!: number;
  public forum!: ForumApiResponseDTO;
  constructor(
              private _forumService: ForumService,
              private _postService: PostService,
              private _authService: AuthServiceService,
              private _router: Router,
              private _activatedRoute:ActivatedRoute
            ){}

  ngOnInit(): void {
    let forumId = this._activatedRoute.snapshot.paramMap.get('id') || "0";
    this.loadForum(forumId);
    this.getUserId()
  }



  public loadForum(forumId: string) {
    this._forumService.getForumById(forumId)
          .subscribe(f => {
            this.forum = f;

          },
        err => {
          console.log(err);
        })
  }

  public navigateToCreatePost(){
    this._router.navigate([`forum/${this.forum.forum.id}/create-post`]);
  }

  public deletePost(postId: number){
    this._postService.deletePost(postId)
      .subscribe(() => {
        let posts : PostResponseDTO[] = [...this.forum.posts];
        let deletedPostIndex = posts.findIndex(p => p.id === postId);
        if(deletedPostIndex !== -1){
          this.forum.posts.splice(deletedPostIndex, 1);
        }
      },
    err => {
      console.log(err);
    })
  }

  public deleteForum(){

    this._forumService.deleteForum(this.forum.forum.id)
      .subscribe(() => {
        this._router.navigate(["forums"]);
      },
    err => {
      console.log(err);
    })
  }

  public goBack(){
    this._router.navigate(["forums"])
  }

  private getUserId(){
    this.userId = this._authService.getUserId();
  }




}
