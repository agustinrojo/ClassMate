import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { PostRequestDTO } from '../../../services/dto/post/post-request-dto.interface';
import { delay } from 'rxjs';
import { FileDTO } from '../../../services/dto/attachment/file-dto.interface';
import { mapFileToFIleDTO } from '../../../mappers/mapFileToFileDTO.mapper';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit{


  public createPostForm!: FormGroup;
  public disableBtn = false;
  public showErr = false;
  public selectedFiles: File[] = [];
  public selectedFileDTOs: FileDTO[] = [];

  constructor(private _fb: FormBuilder,
              private _router:Router,
              private _activateRoute: ActivatedRoute,
              private _postService: PostService ) {}

  ngOnInit(): void {
    this.createPostForm = this._fb.group({
      title : ["", Validators.required, [] ],
      body  : ["", Validators.required, [] ],
      files : ["", [], []]
    })
  }


  public submit(){
    let user = JSON.parse(localStorage.getItem("user") || "{}");
    let post: PostRequestDTO = {
      id: 1,
      authorId: user.id,
      forumId: parseInt(this._activateRoute.snapshot.paramMap.get("id" || "0")!),
      title: this.createPostForm.get("title")?.value,
      body: this.createPostForm.get("body")?.value,
      creationDate: new Date(),
      files: this.selectedFiles
    }

    this._postService.savePost(post)
      .subscribe(() => {
        this._router.navigate([`forum/${post.forumId}`]);
        this.disableBtn = true;
      },
    async err => {
      this.showErr = true;
      await delay(3000);
      this._router.navigate([`forum/${post.forumId}`]);
    })
  }

  public goBack(){
    let forumId: number = parseInt(this._activateRoute.snapshot.paramMap.get("id" || "0")!);
    this._router.navigate([`forum/${forumId}`]);
  }

  public onFileChange(eventEntity: any) {
    if (eventEntity.target.files.length > 0) {
      const files = eventEntity.target.files;
      for (let i = 0; i < files.length; i++) {
        this.selectedFiles.push(files[i]);
        this.selectedFileDTOs.push(mapFileToFIleDTO(files[i]));
      }
    }
  }

  public removeFile(fileIndex: number) {
    this.selectedFileDTOs.splice(fileIndex, 1);
    this.selectedFiles.splice(fileIndex, 1);
  }


  public updatePostBody(newContent: string) {
    this.createPostForm.get("body")!.setValue(newContent);
  }

  get bodyControl(): FormControl {
    return this.createPostForm.get('body') as FormControl;
  }


}
