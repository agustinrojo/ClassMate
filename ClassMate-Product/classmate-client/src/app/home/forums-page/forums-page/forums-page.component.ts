import { state } from '@angular/animations';
import { ForumDTO } from '../../../services/dto/forum/forum-dto.interface';
import { ForumService } from '../../../services/forum.service';
import { Router } from '@angular/router';
import { User } from '../../../auth/dto/user-dto.interface';
import { MessageService } from '../../../services/message.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-forums-page',
  templateUrl: './forums-page.component.html',
  styleUrl: './forums-page.component.css'
})
export class ForumsPageComponent implements OnInit{
  public forums : ForumDTO[] = [];
  public forumsSubscribed! : number[];
  public forumsCreated! : number[];
  public message: string = '';

  constructor(
    private _forumService: ForumService,
    private _router: Router,
    private _messageService: MessageService
  ){

  }

  ngOnInit(): void {
    // Subscribe to the message from the MessageService
    this._messageService.getMessage().subscribe(message => {
      if (message) {
        this.showMessage(message);
      }
    });


    this.getForumsSubscribed();
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

  public toggleSubscription(forumId: number): void {
    if (this.forumsSubscribed.includes(forumId)) {
      this.unsubscribe(forumId);
    } else {
      this.subscribe(forumId);
    }
  }

  public subscribe(forumId: number){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    return this._forumService.addMember(forumId, user.id)
      .subscribe(() => {
        this.forumsSubscribed.unshift(forumId);
        user.forumsSubscribed.unshift(forumId);
        localStorage.setItem("user", JSON.stringify(user));
        this.getForumsSubscribed();
      },
    err => {
      if (err.status === 403) {
        this.showMessage("Estás baneado de este foro");
      }
      console.log(err);
    })
  }

  private showMessage(message: string): void {
    this.message = message;
    setTimeout(() => {
      this.message = ''; // Clear the message after 3 seconds
      this._messageService.clearMessage(); // Clear the message from the service
    }, 3000); // Display the message for 3 seconds
  }


  public navigateToCreateForum(){
    this._router.navigate(["create-forum"]);
  }

  private getForumsSubscribed(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.forumsSubscribed = user.forumsSubscribed;
    this.forumsCreated = user.forumsCreated;
  }

  public unsubscribe(forumId: number): void {
    this._forumService.removeMember(forumId).subscribe(() => {
      this.getForumsSubscribed();
    },
    err => {
      console.log(err);
    });
  }

  public isCreator(forumId: number): boolean {
    return this.forumsCreated.includes(forumId);
  }

}
