import { Component, OnInit } from '@angular/core';
import { CommentService } from '../../../services/comment.service';
import { CommentDeleteRequestDTO } from '../../../services/dto/comment/comment-delete-request-dto.interface';
import { CommentDTOResponse } from '../../../services/dto/comment/comment-response-dto.interface';

@Component({
  selector: 'app-comment-reports',
  templateUrl: './comment-reports.component.html',
  styleUrl: './comment-reports.component.css'
})
export class CommentReportsComponent implements OnInit{

  public reportedComments!: CommentDeleteRequestDTO[];
  public reportedCommentsDisplay: CommentDTOResponse[] = [];

  constructor(private _commentService: CommentService){}

  ngOnInit(): void {
    this.getReportedComments()
  }

  public getReportedComments(){
    this._commentService.getReportedComments()
      .subscribe((reportedComments: CommentDeleteRequestDTO[]) => {
        console.log(reportedComments);
        this.reportedComments = reportedComments;
        this.reportedComments.forEach((comment) => {
          this.reportedCommentsDisplay.push({
            id: comment.id,
            postId: comment.postId,
            forumId: comment.forumId,
            author: comment.author,
            body: comment.body,
            creationDate: comment.creationDate,
            files: comment.files,
            hasBeenEdited: comment.hasBeenEdited,
            valoration: comment.valoration,
            dislikedByUser: false,
            likedByUser: false,
            reportedByUser: false
          })
        })
      },
    (err) => {
      console.log(err);
    })
  }
}
