import { Component, OnInit } from '@angular/core';
import { PostAPIResponseDTO } from '../../../services/dto/post/post-api-response-dto';
import { PostService } from '../../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentDTO } from '../../../services/dto/comment/comment-dto.interface';
import { LoginResponse } from '../../../auth/dto/login-response.interface';
import { CommentService } from '../../../services/comment.service';
import { User } from '../../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrl: './post-page.component.css'
})
export class PostPageComponent implements OnInit{
  public post!: PostAPIResponseDTO;
  public bodyForm!: FormGroup;

  constructor( private _postService: PostService,
               private _activatedRoute: ActivatedRoute,
               private _router: Router,
               private _fb: FormBuilder,
               private _commentService: CommentService){ }

  ngOnInit(): void {
    let postId = this._activatedRoute.snapshot.paramMap.get('id') || "0";
    this.loadPost(postId);

    this.bodyForm = this._fb.group({
      body: ["", Validators.required, []]
    })

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

  public goBackToForum(){
    this._router.navigate([`forum/${this.post.forumId}`])
  }

  public submit(){
    console.log("entro aca")
    let user = JSON.parse(localStorage.getItem("user") || "");

    let newComment: CommentDTO = {
      id: 0,
      postId: this.post.id,
      authorId: user.id,
      body: this.bodyForm.get("body")?.value,
      creationDate: new Date()
    };

    this._commentService.saveComment(newComment)
      .subscribe((comment: CommentDTO) => {
        this.post.commentDTOS.unshift(comment);
        console.log(this.post.commentDTOS)
      },
    err => {
      console.log(err);
    })

  }

  public deleteComment(event: number){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    let userId = user.id;
    this._commentService.deleteComment(event, userId)
      .subscribe(() => {
        //borrar comentario
        const comments : CommentDTO[] = [...this.post.commentDTOS];
        const deletedCommentIndex = comments.findIndex(c => c.id === event);
        if(deletedCommentIndex !== -1){
          this.post.commentDTOS.splice(deletedCommentIndex, 1);
        }
      },
    err => {
      console.log(err);
    })
  }

}
