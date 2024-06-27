import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForumsPageComponent } from './forums-page/forums-page.component';
import { ForumService } from '../../services/forum.service';
import { CreateForumComponent } from './create-forum/create-forum.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditForumComponent } from '../single-forum-page/edit-forum/edit-forum.component';



@NgModule({
  declarations: [
    ForumsPageComponent,
    CreateForumComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    ForumService
  ]
})
export class ForumsPageModule { }
