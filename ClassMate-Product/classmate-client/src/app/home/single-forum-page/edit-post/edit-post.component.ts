import { Component, OnInit } from '@angular/core';
import { tick } from '@angular/core/testing';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ValidationRequest } from '../../../auth/dto/validation-request.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { PostData } from '../../interfaces/post-data.interface';
import { PostService } from '../../../services/post.service';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { PostUpdateDTO } from '../../../services/dto/post/post-update-dto.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css'
})
export class EditPostComponent implements OnInit{

  public editPostForm!: FormGroup;
  public showErr: boolean = false;
  public disableBtn: boolean = false;
  private postId!: number;


  constructor(private _fb: FormBuilder,
              private _router: Router,
              private _activatedRoute: ActivatedRoute,
              private _postStateService: PostStateService,
              private _postService: PostService){}

  ngOnInit(): void {
    this.postId = parseInt(this._activatedRoute.snapshot.paramMap.get("id" || "0")!)
    this._postStateService.getPostData().subscribe((postData : PostData | null) => {
      if(postData) {
        this.editPostForm = this._fb.group({
          title: [postData.title, Validators.required, []],
          body:  [postData.body, Validators.required, []]
        });
      } else {
        this.editPostForm = this._fb.group({
          title: ["", Validators.required, []],
          body:  ["", Validators.required, []]
        });
      }
    })

  }

  public submit() {
    this.disableBtn = true;
    let title = this.editPostForm.get("title")!.value;
    let body  = this.editPostForm.get("body")!.value;
    let updatedPost: PostUpdateDTO = {
      title: title,
      body: body
    }

    this._postService.updatePost(this.postId, updatedPost).subscribe(() => {
      this._router.navigate([`post/${this.postId}`]);
    },
    err => {
      console.log(err);
      this.showErr = true;
      delay(3000);
      this._router.navigate([`post/${this.postId}`]);
    }
  )
  }

  public goBack() {
    this._router.navigate([`post/${this.postId}`])
  }

}
