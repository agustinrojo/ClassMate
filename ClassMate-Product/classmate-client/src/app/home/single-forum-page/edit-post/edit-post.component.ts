import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { PostUpdateDTO } from '../../../services/dto/post/post-update-dto.interface';
import { delay } from 'rxjs';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { mapFileToFIleDTO } from '../../../mappers/mapFileToFileDTO.mapper';
import { PostData } from '../../interfaces/post-data.interface';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {

  public editPostForm!: FormGroup;
  public showErr: boolean = false;
  public disableBtn: boolean = false;
  public postData!: PostData;
  public markedForRemoval: boolean[] = [];
  public fileIdsToRemove: number[] = [];
  public filesToAdd: File[] = [];
  public fileDTOsToAdd: FileDTO[] = [];
  private postId!: number;
  private forumId!: number;

  constructor(private _fb: FormBuilder,
              private _router: Router,
              private _activatedRoute: ActivatedRoute,
              private _postStateService: PostStateService,
              private _postService: PostService) {}

  ngOnInit(): void {
    this.postId = parseInt(this._activatedRoute.snapshot.paramMap.get("id")!)
    this.forumId = parseInt(this._activatedRoute.snapshot.paramMap.get("forumId")!)
    this._postStateService.getPostData().subscribe((postData: PostData | null) => {
      if (postData) {
        this.postData = postData;
        this.editPostForm = this._fb.group({
          title: [postData.title, [Validators.required, Validators.maxLength(300)]],
          body: [postData.body, [Validators.required, Validators.maxLength(5000)]],
          files: ["", []]
        });
      } else {
        this.postData = {
          title: "",
          body: "",
          files: []
        };
        this.editPostForm = this._fb.group({
          title: ["", [Validators.required, Validators.maxLength(300)]],
          body: ["", [Validators.required, Validators.maxLength(5000)]],
          files: ["", []]
        });
      }
    });

    for (const file of this.postData.files) {
      this.markedForRemoval.unshift(false);
    }
  }

  get bodyControl(): FormControl {
    return this.editPostForm.get('body') as FormControl;
  }

  public submit() {
    this.disableBtn = true;
    let title = this.editPostForm.get("title")!.value;
    let body = this.editPostForm.get("body")!.value;
    let updatedPost: PostUpdateDTO = {
      title: title,
      body: body,
      fileIdsToRemove: this.fileIdsToRemove,
      filesToAdd: this.filesToAdd
    };

    this._postService.updatePost(this.postId, updatedPost).subscribe(() => {
      this._router.navigate([`forum/${this.forumId}/post/${this.postId}`]);
    },
    err => {
      console.log(err);
      this.showErr = true;
      delay(3000);
      this._router.navigate([`forum/${this.forumId}/post/${this.postId}`]);
    });
  }

  public goBack() {
    this._router.navigate([`forum/${this.forumId}/post/${this.postId}`]);
  }

  public toggleMarkForRemoval(index: number) {
    let isMarkedForRemoval = this.markedForRemoval[index];

    if (isMarkedForRemoval) {
      this.markedForRemoval[index] = false;
      let fileId: number | undefined = this.fileIdsToRemove.find(fileId => fileId == this.postData.files[index].id);
      if (!(fileId === undefined)) {
        this.fileIdsToRemove.splice(this.fileIdsToRemove.indexOf(fileId), 1);
      }
    } else {
      this.markedForRemoval[index] = true;
      this.fileIdsToRemove.push(this.postData.files[index].id);
    }
  }

  public onFileAdded(eventEntity: any) {
    if (eventEntity.target.files.length > 0) {
      const files = eventEntity.target.files;
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
