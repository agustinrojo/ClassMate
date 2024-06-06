import { Component, Input } from '@angular/core';
import { CommentDTO } from '../../../services/dto/comment/comment-dto.interface';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css'
})
export class CommentComponent {

  @Input() public comment?: CommentDTO;

}
