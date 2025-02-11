import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { User } from '../../../auth/dto/user-dto.interface';
import { CommentDTOResponse } from '../../../services/dto/comment/comment-response-dto.interface';
import { CommentService } from '../../../services/comment.service';
import { CommentUpdateDTO } from '../../../services/dto/comment/comment-update-dto.interface';
import { FileService } from '../../../services/file.service';
import { Valoration } from '../../interfaces/valoration.interface';
import { FileDownloadEvent } from '../../interfaces/file-download-event.interface';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { Role } from '../../../auth/enums/role.enum';
import { MatDialog } from '@angular/material/dialog';
import { DeleteRequestDTO } from '../../../services/dto/delete-request/delete-request.dto';
import { ReportCommentDialogComponent } from '../../../shared/report-comment-dialog/report-comment-dialog.component';
import { DeleteRequestDTOResponse } from '../../../services/dto/delete-request/delete-request-response.dto';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit{

  public userId! : number;
  public isAdmin!: boolean;
  public userRole!: Role;
  public editing: boolean = false;
  public disableBtn: boolean = false;
  public showErr : boolean = false;
  public commentValoration!: Valoration;
  @Input() public comment!: CommentDTOResponse;
  @Input() public deleteRequests?: DeleteRequestDTOResponse[] | null = null;
  @Output() public deleteEvent = new EventEmitter<number>();


  constructor(
    private _commentService: CommentService,
    private _fileService: FileService,
    private _router: Router,
    private _authService: AuthServiceService,
    private _dialog: MatDialog
  ){}

  ngOnInit(): void {
    this.getUserId();
    this.getUserRole();
    this.commentValoration = {
      id: this.comment.id,
      valoration: this.comment.valoration,
      likedByUser: this.comment.likedByUser,
      dislikedByUser: this.comment.dislikedByUser
    }
  }

  public deleteComment(){
    this.deleteEvent.emit(this.comment?.id);
  }

  public editComment(commentUpdate: CommentUpdateDTO){
    this._commentService.updateComment(this.comment.id, commentUpdate).subscribe(()=>{
      this.editing = false;
      this.getComment();
    },
    err => {
      console.log(err)
      this.showErr = true;
    })
  }

  public toggleEditing(){
    this.editing = true;
  }

  public openReportDialog(){
    const dialogRef = this._dialog.open(ReportCommentDialogComponent, {
      width: '250px',
      height: '300px'
    })

    dialogRef.afterClosed().subscribe((message: string) => {
      const deleteRequst: DeleteRequestDTO = {
        reporterId: this.userId,
        message: message
      }

      this._commentService.reportComment(this.comment.id, deleteRequst).subscribe(() => {
        this.comment.reportedByUser = true;
        console.log("report success")
      },
      (err) => {
        console.log(err);
      })
    })
  }

  private getUserId(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.userId = user.id;
    this.isUserAdmin();
    console.log("User is admin? ", this.isAdmin);
    console.log(this.comment.forumId);
  }

  private getUserRole(){
    this.userRole = this._authService.getUser().role;
  }

  private isUserAdmin(): void {
    this._authService.getForumsAdmin().subscribe({
      next: (forumsAdmin: number[]) => {
        // Check if the user is an admin in the forum associated with the current post
        this.isAdmin = forumsAdmin.includes(this.comment.forumId);
      },
      error: (err) => {
        console.error('Error fetching forums admin list', err);
        this.isAdmin = false; // Handle the case where the admin check fails
      }
    });
  }

  public cancelEdition(){
    this.editing = false;
  }

  public getComment(){
    this._commentService.getCommentById(this.comment.id).subscribe((c: CommentDTOResponse) => {
      this.comment = c
    },
  err => {
    console.log(err);
  })
  }

  public downloadFile(file: FileDownloadEvent){
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

  public upvoteComment() {
    if (this.comment.likedByUser) {
      // Already upvoted, no action needed
      return;
    }

    if (this.comment.dislikedByUser) {
      // Remove downvote before upvoting
      this._commentService.removeCommentVote(this.comment.id).subscribe(() => {
        this.executeUpvote();
      }, (err) => {
        console.error("Error removing downvote", err);
      });
    } else {
      // Directly upvote if no downvote exists
      this.executeUpvote();
    }
  }

  private executeUpvote() {
    this._commentService.upvoteComment(this.comment.id).subscribe(() => {
      console.log("Upvote success");
      this.comment.likedByUser = true;
      this.comment.dislikedByUser = false; // Clear any existing downvote
    }, (err) => {
      console.error("Error upvoting comment", err);
    });
  }

  public downvoteComment() {
    if (this.comment.dislikedByUser) {
      // Already downvoted, no action needed
      return;
    }

    if (this.comment.likedByUser) {
      // Remove upvote before downvoting
      this._commentService.removeCommentVote(this.comment.id).subscribe(() => {
        this.executeDownvote();
      }, (err) => {
        console.error("Error removing upvote", err);
      });
    } else {
      // Directly downvote if no upvote exists
      this.executeDownvote();
    }
  }

  private executeDownvote() {
    this._commentService.downvoteComment(this.comment.id).subscribe(() => {
      console.log("Downvote success");
      this.comment.dislikedByUser = true;
      this.comment.likedByUser = false; // Clear any existing upvote
    }, (err) => {
      console.error("Error downvoting comment", err);
    });
  }


  public removeCommentVote() {
    this._commentService.removeCommentVote(this.comment.id).subscribe(() => {
      console.log("Remove vote success");
    },
    err => {
      console.log(err);
    });
  }


  public navigateToUserProfile() {
    this._router.navigate([`profile/${this.comment!.author!.userId}`]);
  }


}
