import { Component, OnInit } from '@angular/core';
import { PostAPIResponseDTO } from '../../../services/dto/post/post-api-response-dto';
import { PostService } from '../../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrl: './post-page.component.css'
})
export class PostPageComponent implements OnInit{
  public post?: PostAPIResponseDTO;

  constructor( private _postService: PostService, private _activatedRoute: ActivatedRoute, _router: Router ){ }

  ngOnInit(): void {
    let postId = this._activatedRoute.snapshot.paramMap.get('id') || "0";
    this.loadPost(postId);
  }

  public loadPost(postId: string){
    this._postService.getPostById(postId)
    .subscribe(p => {
      this.post = p;

    },
  err => {
    console.log(err);
  })
  }

}
