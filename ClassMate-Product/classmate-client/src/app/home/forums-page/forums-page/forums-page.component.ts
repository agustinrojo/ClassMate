import { Component, OnInit } from '@angular/core';
import { ForumDTO } from '../../../services/dto/forum/forum-dto.interface';
import { ForumService } from '../../../services/forum.service';
import { Router } from '@angular/router';
import { User } from '../../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-forums-page',
  templateUrl: './forums-page.component.html',
  styleUrl: './forums-page.component.css'
})
export class ForumsPageComponent implements OnInit{
  public forums : ForumDTO[] = [];
  public forumsScubscribed! : number[];

  constructor(private _forumService: ForumService, private _router: Router){

  }

  ngOnInit(): void {
    this.loadForums();
    this.getForumsSubscribed();
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

  public subscribe(forumId: number){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    return this._forumService.addMember(forumId, user.id)
      .subscribe(() => {
        user.forumsSubscribed.unshift(forumId);
        localStorage.setItem("user", JSON.stringify(user));
        this.getForumsSubscribed();
      },
    err => {
      console.log(err);
    })
  }

  public navigateToCreateForum(){
    this._router.navigate(["create-forum"]);
  }

  private getForumsSubscribed(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.forumsScubscribed = user.forumsSubscribed;
  }

}
