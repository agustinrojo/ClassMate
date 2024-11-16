import { Component, Input, OnInit } from '@angular/core';
import { ForumService } from '../../../services/forum.service';
import { MultipleForumRequestDTO } from '../../../services/dto/forum/multiple-forum-request-dto.interface';
import { ForumDTO } from '../../../services/dto/forum/forum-dto.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forum-list',
  templateUrl: './forum-list.component.html',
  styleUrl: './forum-list.component.css'
})
export class ForumListComponent implements OnInit{
  @Input() public forumsSubcribedIds?: number[];
  public forums: ForumDTO[] = [];
  public showErr: boolean = false;
  public loading: boolean = true;

  constructor(private _forumService: ForumService, private _router: Router){
  }

  ngOnInit(): void {
    if(this.forumsSubcribedIds){
      this._forumService.getMultipleForums(this.forumsSubcribedIds).subscribe((forums: ForumDTO[]) => {
        console.log(forums);
        this.forums = forums;
      },
      (err) => {
        console.log(err);
        this.showErr = true;
      })
    }
    this.loading = false;
  }

  public navigateToForum(forumId: number){
    this._router.navigate([`forum/${forumId}`]);
  }

}
