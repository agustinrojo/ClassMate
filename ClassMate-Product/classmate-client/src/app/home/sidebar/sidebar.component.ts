import { Component, Input, OnInit } from '@angular/core';
import { ForumData } from '../../services/dto/forum/forum-data-dto.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent{
  @Input() public forums!: ForumData[];

  constructor(private _router: Router){}

  public navigatetoForum(index: number){
    this._router.navigate([`forum/${this.forums[index].id}`]);
  }


}
