import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';


import { Router } from '@angular/router';
import { PostRequestDTO } from '../../../services/dto/post/post-request-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { PostData } from '../../interfaces/post-data.interface';
import { state } from '@angular/animations';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { FileDownloadEvent } from '../../interfaces/file-download-event.interface';
import { FileService } from '../../../services/file.service';
import { Valoration } from '../../interfaces/valoration.interface';
import { PostService } from '../../../services/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent implements OnInit{
  public userId!: number;
  public isAdmin!: boolean;
  public postValoration!: Valoration;
  @Input() public post!: PostResponseDTO;
  @Output() public deleteEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor(private _authService: AuthServiceService,
              private _fileService: FileService,
              private _router: Router,
              private _postService: PostService
            ) {}

  ngOnInit(): void {
    this.setPostValoration();
  }

private setPostValoration(): void {
  this.getUserId();
  this.postValoration = {
    id: this.post.id,
    valoration: this.post.valoration,
    likedByUser: this.post.likedByUser,
    dislikedByUser: this.post.dislikedByUser
  }
}
  public navigateToPost(postId: number){
    this._router.navigate([`forum/${this.post.forumId}/post/${postId}`]);
  }

  public deletePost() {
    this.deleteEvent.emit(this.post?.id);
  }

  public getUserId(){
    this.userId = this._authService.getUserId();
    this.isUserAdmin();
  }

  private isUserAdmin(): void {
    this._authService.getForumsAdmin().subscribe({
      next: (forumsAdmin: number[]) => {
        // Check if the user is an admin in the forum associated with the current post
        this.isAdmin = forumsAdmin.includes(this.post.forumId);
      },
      error: (err) => {
        console.error('Error fetching forums admin list', err);
        this.isAdmin = false; // Handle the case where the admin check fails
      }
    });
  }


  public downloadPostFile(file: FileDownloadEvent) {
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

  public upvotePost() {
    if(this.post.likedByUser){
      return;
    }
    this._postService.upvotePost(this.post.id).subscribe(() => {
      console.log("Upvote success")
    },
  err => {
    console.log(err);
  })
  }

  public downvotePost() {
    if(this.post.dislikedByUser){
      return;
    }
    this._postService.downvotePost(this.post.id).subscribe(() => {
      console.log("Downvote success")
    },
  err => {
    console.log(err);
  })
  }

  public removePostVote() {

    this._postService.removePostVote(this.post.id).subscribe(() => {
      console.log("Remove success")
    },
  err => {
    console.log(err);
  })
  }

  public navigateToUserProfile(){
    this._router.navigate([`/profile/${this.post.author.userId}`])
  }

}
