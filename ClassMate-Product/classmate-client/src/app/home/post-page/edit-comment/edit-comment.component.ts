import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentService } from '../../../services/comment.service';
import { CommentUpdateDTO } from '../../../services/dto/comment/comment-update-dto.interface';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { mapFileToFIleDTO } from '../../../mappers/mapFileToFileDTO.mapper';

@Component({
  selector: 'app-edit-comment',
  templateUrl: './edit-comment.component.html',
  styleUrl: './edit-comment.component.css'
})
export class EditCommentComponent implements OnInit{
  public editCommentForm!: FormGroup;
  public disableBtn: boolean = false;
  public filesToAdd: File[] = [];
  public fileDTOsToAdd: FileDTO[] = [];
  public fileIdsToRemove: number[] = [];
  public markedForRemoval: boolean[] = [];
  @Input() public body!: string;
  @Input() public files!: FileDTO[];
  @Output() public cancelEditing: EventEmitter<void> = new EventEmitter<void>();
  @Output() public editComment: EventEmitter<CommentUpdateDTO> = new EventEmitter<CommentUpdateDTO>();

  constructor(
              private _fb: FormBuilder
              ){}

  ngOnInit(): void {

    for (const file of this.files) {
      this.markedForRemoval.unshift(false);
    }

    if(this.body){
      this.editCommentForm = this._fb.group({
        body: [this.body, Validators.required, []]
      })
    } else {
      this.editCommentForm = this._fb.group({
        body: ["", Validators.required, []]
      })
    }
  }

  public cancel() {
    this.cancelEditing.emit();
    this.fileIdsToRemove = [];
    this.filesToAdd = [];
    this.fileDTOsToAdd = [];
  }

  public confirmEdition(){
    let commentUpdate :  CommentUpdateDTO = {
      body: this.editCommentForm.get("body")!.value,
      filesToAdd: this.filesToAdd,
      fileIdsToRemove: this.fileIdsToRemove
    }
    this.editComment.emit(commentUpdate);
  }

  public toggleMarkForRemoval(index: number){
    let isMarkedForRemoval = this.markedForRemoval[index];

    if(isMarkedForRemoval) {
      this.markedForRemoval[index] = false;
      let fileId: number | undefined = this.fileIdsToRemove.find(fileId => fileId == this.files[index].id);
      if(!(fileId === undefined)){
        this.fileIdsToRemove.splice(this.fileIdsToRemove.indexOf(fileId), 1);
      }
    } else {
      this.markedForRemoval[index] = true;
      this.fileIdsToRemove.push(this.files[index].id);
    }

  }

  public onFileAdded(event: any) {
    if (event.target.files.length > 0) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        this.filesToAdd.push(files[i]);
        this.fileDTOsToAdd.push(mapFileToFIleDTO(files[i]));
      }

    }
  }

  public removeUploadedFile(fileIndex: number){
    this.fileDTOsToAdd.splice(fileIndex, 1);
    this.filesToAdd.splice(fileIndex, 1);
  }



}
