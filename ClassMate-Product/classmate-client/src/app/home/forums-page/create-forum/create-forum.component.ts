import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ForumService } from '../../../services/forum.service';
import { Router } from '@angular/router';
import { ForumRequestDTO } from '../../../services/dto/forum/create/forum-request-dto.interface';
import { User } from '../../../auth/dto/user-dto.interface';
import { delay } from 'rxjs';
import { ForumData } from '../../interfaces/forum-data.interface';
import { ForumDataSidebar } from '../../../services/dto/forum/forum-data-dto.interface';
import { ForumStateService } from '../../../services/dto/state-services/forum-state.service';

@Component({
  selector: 'app-create-forum',
  templateUrl: './create-forum.component.html',
  styleUrl: './create-forum.component.css'
})
export class CreateForumComponent implements OnInit{
  public createForumForm!: FormGroup;
  public btnDisable: boolean = false;
  public showErr: boolean = false;

  constructor(private _forumService: ForumService,
              private _fb: FormBuilder,
              private _router: Router,
              private forumStateService: ForumStateService
  ) { }

  ngOnInit(): void {
    this.createForumForm = this._fb.group({
      title: ["", Validators.required, []],
      description: ["", Validators.required, []]
    })
  }

  public createForum(){
    this.btnDisable = true;
    let forum: ForumRequestDTO = {
      title: this.createForumForm.get("title")?.value,
      description: this.createForumForm.get("description")?.value
    }
    let user: User = JSON.parse(localStorage.getItem("user")!);
    let userId = user.id;
    this._forumService.saveForum(forum, userId)
      .subscribe((f) => {
        user.forumsSubscribed.push(f.id);
        user.forumsCreated.push(f.id);
        localStorage.setItem("user", JSON.stringify(user));
        let forumData: ForumDataSidebar = {
          id: f.id,
          title: f.title
        }
        this.forumStateService.setForumCreationEvent(forumData);
        this._router.navigate([`forum/${f.id}`]);
      },
    async err => {
      this.showErr = true;
      await delay(3000);
      this._router.navigate(["forums"]);
    } )
  }

  public goBack(){
    this._router.navigate(["forums"]);
  }



}
