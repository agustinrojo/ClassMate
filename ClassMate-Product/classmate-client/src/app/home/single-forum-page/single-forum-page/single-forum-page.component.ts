import { Component, OnInit } from '@angular/core';
import { ForumService } from '../../../services/forum.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumApiResponseDTO } from '../../../services/dto/forum/forum-api-response-dto.interface';

@Component({
  selector: 'app-single-forum-page',
  templateUrl: './single-forum-page.component.html',
  styleUrl: './single-forum-page.component.css'
})
export class SingleForumPageComponent implements OnInit{

  public forum?: ForumApiResponseDTO;

  constructor(private _forumService: ForumService, private _router: Router, private _activatedRoute:ActivatedRoute){}

  ngOnInit(): void {
    let forumId = this._activatedRoute.snapshot.paramMap.get('id') || "0";
    this.loadForum(forumId);
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



}
