import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';


import { Router } from '@angular/router';
import { PostRequestDTO } from '../../../services/dto/post/post-request-dto.interface';
import { AuthServiceService } from '../../../auth/auth-service.service';
import { PostData } from '../../interfaces/post-data.interface';
import { state } from '@angular/animations';
import { PostStateService } from '../../../services/dto/state-services/post-state.service';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { FileDownloadEvent } from '../../interfaces/file-download-event.interface';
import { FileService } from '../../../services/file.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
})
export class PostComponent implements OnInit{


  public userId!: number;
  @Input() public post?: PostResponseDTO;
  @Output() public deleteEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor(private _authService: AuthServiceService,
              private _fileService: FileService,
              private _router: Router,
              private _postStateService: PostStateService) {}

  ngOnInit(): void {
    this.getUserId();
  }


  public navigateToPost(postId: number){
    this._router.navigate([`post/${postId}`]);
  }

  public deletePost() {
    this.deleteEvent.emit(this.post?.id);
  }

  public getUserId(){
    this.userId = this._authService.getUserId();
  }

  downloadPostFile(file: FileDownloadEvent) {
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
