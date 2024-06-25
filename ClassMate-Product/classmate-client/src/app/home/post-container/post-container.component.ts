import { Component, OnInit } from '@angular/core';
import { PostResponseDTO } from '../../services/dto/post/post-response-dto.interface';
import { AuthServiceService } from '../../auth/auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumApiResponseDTO } from '../../services/dto/forum/forum-api-response-dto.interface';
import { ForumData } from '../../services/dto/forum/forum-data-dto.interface';

@Component({
  selector: 'app-post-container',
  templateUrl: './post-container.component.html',
  styleUrl: './post-container.component.css'
})
export class PostContainerComponent implements OnInit{

  public forums: ForumData[] = [];
  public posts: PostResponseDTO[] = [];

  constructor(private _authService: AuthServiceService,
              private _forumsService: ForumService
  ){ }

  ngOnInit(): void {
    console.log("llego aca")
    let forumsSubscribed: number[] = this._authService.getForumsSubscibed();
    console.log(forumsSubscribed)
    for (const forumId of forumsSubscribed) {
      this._forumsService.getForumById(forumId.toString()).subscribe((forum: ForumApiResponseDTO) => {
        let forumData: ForumData = {
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

  }

}
