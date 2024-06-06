import { Component, OnInit } from '@angular/core';
import { ForumDTO } from '../../../services/dto/forum/forum-dto.interface';
import { ForumService } from '../../../services/forum.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forums-page',
  templateUrl: './forums-page.component.html',
  styleUrl: './forums-page.component.css'
})
export class ForumsPageComponent implements OnInit{
  public forums : ForumDTO[] = [];

  constructor(private _forumService: ForumService, private _router: Router){

  }

  ngOnInit(): void {
    this.loadForums();
  }

  public loadForums() : ForumDTO[]{

    this._forumService.getForums()
      .subscribe( f => {
        this.forums = f;
      },
    err => {
      console.log(err);
    })

    return [];
  }

  public navigateToForum(id : number){
    this._router.navigate([`forum/${id}`])
  }

}
