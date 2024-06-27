import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ForumService } from '../../../services/forum.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ForumStateService } from '../../../services/dto/state-services/forum-state.service';
import { ForumData } from '../../interfaces/forum-data.interface';
import { ForumRequestDTO } from '../../../services/dto/forum/create/forum-request-dto.interface';
import { debounceTime, delay } from 'rxjs';

@Component({
  selector: 'app-edit-forum',
  templateUrl: './edit-forum.component.html',
  styleUrl: './edit-forum.component.css'
})
export class EditForumComponent implements OnInit{
  public forumId!: number;
  public editForumForm!: FormGroup;
  public disableBtn: boolean = false;
  public showerr: boolean = false;

  constructor(
    private _fb: FormBuilder,
    private _forumService: ForumService,
    private _forumStateService: ForumStateService,
    private _router: Router,
    private _activatedRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    this.forumId = parseInt(this._activatedRoute.snapshot.paramMap.get("id" || "0")!)
    this._forumStateService.getForumData().subscribe((forumData: ForumData | null) => {
      if(forumData){
        this.editForumForm = this._fb.group({
          title: [forumData.title, Validators.required , []],
          description: [forumData.description, Validators.required, []]
        })
      } else {
        this.editForumForm = this._fb.group({
          title: ["", Validators.required , []],
          description: ["", Validators.required, []]
        })
      }
    })

  }

  public editForum(){
    let forumUpdate: ForumRequestDTO = {
      title: this.editForumForm.get("title")!.value,
      description: this.editForumForm.get("description")!.value
    }
    this._forumService.updateForum(this.forumId, forumUpdate).subscribe(() => {
      this._router.navigate([`forum/${this.forumId}`]);
    },
    err => {
      this.showerr = true;
      debounceTime(3000);
      this._router.navigate([`forum/${this.forumId}`]);
    })
  }



  public goBack(){
    this._router.navigate([`forum/${this.forumId}`]);
  }


}
