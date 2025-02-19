

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
import { Role } from '../../../auth/enums/role.enum';
import { MatDialog } from '@angular/material/dialog';
import { ReportCommentDialogComponent } from '../../../shared/report-comment-dialog/report-comment-dialog.component';
import { DeleteRequestDTO } from '../../../services/dto/delete-request/delete-request.dto';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DeleteRequestDTOResponse } from '../../../services/dto/delete-request/delete-request-response.dto';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent implements OnInit{
  public userId!: number;
  public userRole!: Role;
  public isAdmin!: boolean;
  public postValoration!: Valoration;
  @Input() public post!: PostResponseDTO;
  @Input() public deleteRequests?: DeleteRequestDTOResponse[] | null = null;
  @Input() public canAbsolve: boolean = false;
  @Output() public deleteEvent: EventEmitter<number> = new EventEmitter<number>();
  @Output() public absolvePostEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor(private _authService: AuthServiceService,
              private _fileService: FileService,
              private _router: Router,
              private _postService: PostService,
              private _dialog: MatDialog
            ) {}

  ngOnInit(): void {
    this.setPostValoration();
  }

private setPostValoration(): void {
  this.getUserId();
  this.getUserRole()
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

  private getUserRole(){
    this.userRole = this._authService.getUser().role;
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

    if (this.post.dislikedByUser) {
      // If there's an active downvote, remove it first
      this._postService.removePostVote(this.post.id).subscribe(() => {
        this.executeUpvote();
      }, (err) => {
        console.error("Error removing downvote", err);
      });
    } else {
      // Directly upvote if no downvote exists
      this.executeUpvote();
    }
    this._postService.upvotePost(this.post.id).subscribe(() => {
      console.log("Upvote success")
    },
  err => {
    console.log(err);
  })
  }

  public downvotePost() {
    if (this.post.dislikedByUser) {
      // Already downvoted, no action needed
      return;
    }

    if (this.post.likedByUser) {
      // If there's an active upvote, remove it first
      this._postService.removePostVote(this.post.id).subscribe(() => {
        console.log("Upvote removed, proceeding to downvote");
        this.executeDownvote();
      }, (err) => {
        console.error("Error removing upvote", err);
      });
    } else {
      // Directly downvote if no upvote exists
      this.executeDownvote();
    }
  }

    public openReportDialog() {
      const dialogRef = this._dialog.open(ReportCommentDialogComponent, {
        width: '250px',
        height: '300px'
      });

      dialogRef.afterClosed().subscribe((message: string) => {
        const deleteRequest: DeleteRequestDTO = {
          message: message,
          reporterId: this.userId
        }
        this._postService.reportPost(this.post.id, deleteRequest).subscribe(() => {
          this.post.reportedByUser = true;
          console.log("report success")
        },
        (err) => {
          console.log(err);
        })
      })
    }

  private executeUpvote() {
    this._postService.upvotePost(this.post.id).subscribe(() => {
      console.log("Upvote success");
      this.post.likedByUser = true;
      this.post.dislikedByUser = false; // Clear any existing state
    }, (err) => {
      console.error("Error upvoting post", err);
    });
  }

  private executeDownvote() {
    this._postService.downvotePost(this.post.id).subscribe(() => {
      console.log("Downvote success");
      this.post.dislikedByUser = true;
      this.post.likedByUser = false; // Clear any existing state
    }, (err) => {
      console.error("Error downvoting post", err);
    });
  }

  public removePostVote() {

    this._postService.removePostVote(this.post.id).subscribe(() => {
      console.log("Remove success")
    },
  err => {
    console.log(err);
  })
  }

  public absolvePost(){
    this._postService.absolvePost(this.post.id).subscribe(() => {
      console.log("absolve success");
      this.absolvePostEvent.emit(this.post.id);
    },
    (err) => {
      console.log(err);
    })
  }

  public navigateToUserProfile(){
    this._router.navigate([`/profile/${this.post.author.userId}`])
  }

}
