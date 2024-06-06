import { Component, Input } from '@angular/core';


import { Router } from '@angular/router';
import { PostDTO } from '../../../services/dto/post/post-dto.interface';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent {

  @Input() public post?: PostDTO;

  constructor(private _router: Router) {}

  public navigateToPost(postId: number){
    this._router.navigate([`post/${postId}`]);
  }

}
