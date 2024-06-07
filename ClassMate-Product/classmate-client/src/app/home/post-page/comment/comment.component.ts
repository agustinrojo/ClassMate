import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommentDTO } from '../../../services/dto/comment/comment-dto.interface';
import { User } from '../../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent implements OnInit{

  public userId! : number;
  @Input() public comment!: CommentDTO;
  @Output() public deleteEvent = new EventEmitter<number>();


  ngOnInit(): void {
    this.getUserId();

  }

  public deleteComment(){
    console.log("llego aca")
    this.deleteEvent.emit(this.comment?.id);
  }

  private getUserId(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.userId = user.id;
  }


}
