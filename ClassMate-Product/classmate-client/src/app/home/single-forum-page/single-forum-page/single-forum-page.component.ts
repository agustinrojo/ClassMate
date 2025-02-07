import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { ForumService } from '../../../services/forum.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumApiResponseDTO } from '../../../services/dto/forum/forum-api-response-dto.interface';
import { PostService } from '../../../services/post.service';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { ForumStateService } from '../../../services/dto/state-services/forum-state.service';
import { ForumData } from '../../interfaces/forum-data.interface';
import { MessageService } from '../../../services/message.service';
import { Role } from '../../../auth/enums/role.enum';

@Component({
  selector: 'app-single-forum-page',
  templateUrl: './single-forum-page.component.html',
  styleUrl: './single-forum-page.component.css'
})
export class SingleForumPageComponent implements OnInit{
  public userId!: number;
  public forum!: ForumApiResponseDTO;
  public userRole!: Role;

  constructor(
              private _forumService: ForumService,
              private _postService: PostService,
              private _authService: AuthServiceService,
              private _forumStateService: ForumStateService,
              private _router: Router,
              private _activatedRoute:ActivatedRoute,
              private _messageService: MessageService
            ){}


  ngOnInit(): void {
     // Subscribe to changes in route parameters
     this._activatedRoute.params.subscribe(params => {
      const forumId = params['id'];
      if (forumId) {
        this.loadForum(forumId);
      }
    });
    this.getUserId();
    this.getUserRole();
  }


  public loadForum(forumId: string) {
    this._forumService.getForumById(forumId)
          .subscribe(f => {
            this.forum = f;  // `f.creator` and `f.admin` should now be available
            this._forumStateService.setCurrentForumData({
              id: this.forum.forum.id,
              title: this.forum.forum.title
            });
          },
        err => {
          if (err.status === 403) {
            // Redirect back to the forums page and pass the message
            this._messageService.setMessage('Estás baneado de este foro');
            this._router.navigate(['/forums']);
          }
          console.log(err);
        });
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

  public editForum(){
    let forumData: ForumData = {
      title: this.forum.forum.title,
      description: this.forum.forum.description
    }
    this._forumStateService.setForumData(forumData);
    this._router.navigate([`forum/edit/${this.forum.forum.id}`]);
  }

  public goBack(){
    this._router.navigate(["forums"])
  }

  private getUserId(){
    this.userId = this._authService.getUserId();
  }

  private getUserRole(){
    this.userRole = this._authService.getUser().role;
  }



}
