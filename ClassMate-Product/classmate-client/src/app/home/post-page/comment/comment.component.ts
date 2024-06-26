import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommentDTORequest } from '../../../services/dto/comment/comment-request-dto.interface';
import { User } from '../../../auth/dto/user-dto.interface';
import { CommentDTOResponse } from '../../../services/dto/comment/comment-response-dto.interface';
import { CommentService } from '../../../services/comment.service';
import { CommentUpdateDTO } from '../../../services/dto/comment/comment-update-dto.interface';
import { FileService } from '../../../services/file.service';
import { FileDownloadEvent } from '../../interfaces/file-download-event.interface';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit{

  public userId! : number;
  public editing: boolean = false;
  public disableBtn: boolean = false;
  public showErr : boolean = false;
  @Input() public comment!: CommentDTOResponse;
  @Output() public deleteEvent = new EventEmitter<number>();


  constructor(
    private _commentService: CommentService,
    private _fileService: FileService
  ){}

  ngOnInit(): void {
    console.log(this.comment)
    this.getUserId();

  }

  public deleteComment(){
    console.log("llego aca")
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

  private getUserId(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.userId = user.id;
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

}
