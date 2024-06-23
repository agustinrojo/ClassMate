import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { PostDTO } from '../../../services/dto/post/post-dto.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit{

  public createPostForm!: FormGroup;
  public disableBtn = false;
  public showErr = false;

  constructor(private _fb: FormBuilder,
              private _router:Router,
              private _activateRoute: ActivatedRoute,
              private _postService: PostService ) {}

  ngOnInit(): void {
    this.createPostForm = this._fb.group({
      title : ["", Validators.required, [] ],
      body  : ["", Validators.required, [] ]
    })
  }


  public submit(){
    let user = JSON.parse(localStorage.getItem("user") || "{}");
    let post: PostDTO = {
      id: 1,
      authorId: user.id,
      forumId: parseInt(this._activateRoute.snapshot.paramMap.get("id" || "0")!),
      title: this.createPostForm.get("title")?.value,
      body: this.createPostForm.get("body")?.value,
      creationDate: new Date()
    }

    this._postService.savePost(post)
      .subscribe(() => {
        this._router.navigate([`forum/${post.forumId}`]);
        this.disableBtn = true;
      },
    async err => {
      this.showErr = true;
      await delay(3000);
      this._router.navigate([`forum/${post.forumId}`]);
    })
  }

  public goBack(){
    let forumId: number = parseInt(this._activateRoute.snapshot.paramMap.get("id" || "0")!);
    this._router.navigate([`forums/${forumId}`]);
  }




}
