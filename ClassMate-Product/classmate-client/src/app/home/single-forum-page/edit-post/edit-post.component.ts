import { Component, OnInit } from '@angular/core';
import { tick } from '@angular/core/testing';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ValidationRequest } from '../../../auth/dto/validation-request.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { PostData } from '../../interfaces/post-data.interface';
import { PostService } from '../../../services/post.service';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { PostUpdateDTO } from '../../../services/dto/post/post-update-dto.interface';
import { delay } from 'rxjs';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { mapFileToFIleDTO } from '../../../mappers/mapFileToFileDTO.mapper';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css'
})
export class EditPostComponent implements OnInit{



  public editPostForm!: FormGroup;
  public showErr: boolean = false;
  public disableBtn: boolean = false;
  public postData!: PostData;
  public markedForRemoval: boolean[] = [];
  public fileIdsToRemove: number[] = [];
  public filesToAdd: File[] = [];
  public fileDTOsToAdd: FileDTO[] = [];
  private postId!: number;



  constructor(private _fb: FormBuilder,
              private _router: Router,
              private _activatedRoute: ActivatedRoute,
              private _postStateService: PostStateService,
              private _postService: PostService){}

  ngOnInit(): void {
    this.postId = parseInt(this._activatedRoute.snapshot.paramMap.get("id" || "0")!)
    this._postStateService.getPostData().subscribe((postData : PostData | null) => {
      if(postData) {
        this.postData = postData;
        this.editPostForm = this._fb.group({
          title: [postData.title, Validators.required, []],
          body:  [postData.body, Validators.required, []],
          files: ["", [], []]
        });
      } else {
        this.postData = {
          title: "",
          body: "",
          files: []
        };
        this.editPostForm = this._fb.group({
          title: ["", Validators.required, []],
          body:  ["", Validators.required, []],
          files: ["", [], []]
        });
      }
    })

    for(const file of this.postData.files){
      this.markedForRemoval.unshift(false)
    }

  }

  public submit() {
    this.disableBtn = true;
    let title = this.editPostForm.get("title")!.value;
    let body  = this.editPostForm.get("body")!.value;
    let updatedPost: PostUpdateDTO = {
      title: title,
      body: body,
      fileIdsToRemove: this.fileIdsToRemove,
      filesToAdd: this.filesToAdd
    }

    this._postService.updatePost(this.postId, updatedPost).subscribe(() => {
      this._router.navigate([`post/${this.postId}`]);
    },
    err => {
      console.log(err);
      this.showErr = true;
      delay(3000);
      this._router.navigate([`post/${this.postId}`]);
    }
  )
  }

  public goBack() {
    this._router.navigate([`post/${this.postId}`])
  }

  public toggleMarkForRemoval(index: number) {
    let isMarkedForRemoval = this.markedForRemoval[index];

    if(isMarkedForRemoval) {
      this.markedForRemoval[index] = false;
      let fileId: number | undefined = this.fileIdsToRemove.find(fileId => fileId == this.postData.files[index].id);
      if(!(fileId === undefined)){
        this.fileIdsToRemove.splice(this.fileIdsToRemove.indexOf(fileId), 1);
      }
    } else {
      this.markedForRemoval[index] = true;
      this.fileIdsToRemove.push(this.postData.files[index].id);
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


  public removeUploadedFile(fileIndex: number) {
    this.fileDTOsToAdd.splice(fileIndex, 1);
    this.filesToAdd.splice(fileIndex, 1);
  }

}
