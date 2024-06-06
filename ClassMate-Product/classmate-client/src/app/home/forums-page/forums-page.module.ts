import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ForumsPageComponent } from './forums-page/forums-page.component';
import { ForumService } from '../../services/forum.service';



@NgModule({
  declarations: [
    ForumsPageComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    ForumService
  ]
})
export class ForumsPageModule { }
