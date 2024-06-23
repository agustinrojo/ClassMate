import { Component, OnInit } from '@angular/core';
import { PostAPIResponseDTO } from '../../../services/dto/post/post-api-response-dto';
import { PostService } from '../../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentDTORequest } from '../../../services/dto/comment/comment-request-dto.interface';
import { LoginResponse } from '../../../auth/dto/login-response.interface';
import { CommentService } from '../../../services/comment.service';
import { User } from '../../../auth/dto/user-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { PostData } from '../../interfaces/post-data.interface';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { CommentDTOResponse } from '../../../services/dto/comment/comment-response-dto.interface';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { FileDownloadEvent } from '../../interfaces/file-download-event.interface';
import { FileService } from '../../../services/file.service';

@Component({
  selector: 'app-post-page',
  templateUrl: './post-page.component.html',
  styleUrl: './post-page.component.css'
})
export class PostPageComponent implements OnInit{
  public post!: PostAPIResponseDTO;
  public bodyForm!: FormGroup;
  public userId! : number;
  public selectedFiles: File[] = [];
  public fileDTOs: FileDTO[] = [];

  constructor( private _postService: PostService,
               private _authService: AuthServiceService,
               private _commentService: CommentService,
               private _postStateService: PostStateService,
               private _fileService: FileService,
               private _activatedRoute: ActivatedRoute,
               private _router: Router,
               private _fb: FormBuilder,
               ){ }

  ngOnInit(): void {
    this.userId = this._authService.getUserId();
    let postId = this._activatedRoute.snapshot.paramMap.get('id') || "0";
    this.loadPost(postId);

    this.bodyForm = this._fb.group({
      body: ["", Validators.required, []],
      files: []
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
    let user = JSON.parse(localStorage.getItem("user") || "");

    let newComment: CommentDTORequest = {
      id: 0,
      postId: this.post.id,
      authorId: user.id,
      body: this.bodyForm.get("body")?.value,
      creationDate: new Date(),
      files: [...this.selectedFiles]
    };

    this.bodyForm.reset();
    this.selectedFiles = [];
    this.fileDTOs = [];

    this._commentService.saveComment(newComment)
      .subscribe((comment: CommentDTOResponse) => {
        this.post.commentDTOS.unshift(comment);
        console.log(this.post.commentDTOS)
      },
    err => {
      console.log(err);
    })

  }

  public deleteComment(event: number){
    let userId: number = this._authService.getUserId();
    this._commentService.deleteComment(event, userId)
      .subscribe(() => {
        //borrar comentario
        const comments : CommentDTOResponse[] = [...this.post.commentDTOS];
        const deletedCommentIndex = comments.findIndex(c => c.id === event);
        if(deletedCommentIndex !== -1){
          this.post.commentDTOS.splice(deletedCommentIndex, 1);
        }
      },
    err => {
      console.log(err);
    })
  }

  public deletePost(){
    this._postService.deletePost(this.post.id)
      .subscribe(() => {
        this._router.navigate([`/forum/${this.post.forumId}`])
      },
    err => {
      console.log(err);
    })
  }

  public navigateToEditPost(){
    let postData: PostData = {
      title: this.post!.title,
      body : this.post!.body
    }
    this._postStateService.setPostData(postData);
    this._router.navigate([`post/edit/${this.post?.id}`]);
  }

  public onFileChange(event: any) {
    if (event.target.files.length > 0) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        this.selectedFiles.push(files[i]);
        this.fileDTOs.push(this.mapFileToFIleDTO(files[i]));
      }

    }
  }

  public removeFile(index: number) {
    this.selectedFiles.splice(index, 1);
    this.fileDTOs.splice(index, 1);
  }

  public downloadPostFile(file: FileDownloadEvent){
    this._fileService.downloadFile(file.fileId).subscribe((blob: Blob) => {
      const a = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      a.href = objectUrl;
      a.download = file.fileName;
      document.body.appendChild(a);  // Añadir el elemento al DOM
      a.click();
      URL.revokeObjectURL(objectUrl);
      document.body.removeChild(a);  // Eliminar el elemento del DOM
    }, err => {
      console.log(err);
    })
  }

  public mapFileToFIleDTO(file: File){
    let fileDTO: FileDTO = {
      id: 0,
      originalFilename: file.name,
      contentType: file.type,
      size: file.size
    }
    console.log(fileDTO)
    return fileDTO;
  }



}
