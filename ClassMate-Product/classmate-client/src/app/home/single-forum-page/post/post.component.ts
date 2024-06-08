import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';


import { Router } from '@angular/router';
import { PostDTO } from '../../../services/dto/post/post-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent implements OnInit{

  public userId!: number;
  @Input() public post?: PostDTO;
  @Output() public deleteEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor(private _authService: AuthServiceService, private _router: Router) {}

  ngOnInit(): void {
    this.getUserId();
  }


  public navigateToPost(postId: number){
    this._router.navigate([`post/${postId}`]);
  }

  public deletePost() {
    this.deleteEvent.emit(this.post?.id);
  }

  public getUserId(){
    this.userId = this._authService.getUserId();
  }


}
