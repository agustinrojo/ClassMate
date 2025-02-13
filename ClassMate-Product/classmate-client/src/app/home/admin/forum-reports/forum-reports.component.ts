import { Component, OnInit } from '@angular/core';
import { ForumDeleteRequestDTOResponse } from '../../../services/dto/forum/forum-delete-request-dto-response.interface';
import { ForumService } from '../../../services/forum.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forum-reports',
  templateUrl: './forum-reports.component.html',
  styleUrl: './forum-reports.component.css'
})
export class ForumReportsComponent implements OnInit{

  public reportedForums!: ForumDeleteRequestDTOResponse[];
  public query: string = "";

  ngOnInit(): void {
    this.getReportedForums();
  }

  constructor(private _forumService: ForumService, private _router: Router){}

  public navigateToForum(id : number){
    this._router.navigate([`forum/${id}`])
  }

  private getReportedForums(){
    this._forumService.getReportedForums().subscribe((forums: ForumDeleteRequestDTOResponse[]) => {
      console.log(forums)
      this.reportedForums = forums;
    },
    (err) => {
      console.log(err);
    })
  }

  public getReportedForumsByKeyword() {
    if(this.query != ""){
      this._forumService.getReportedForumsByKeyword(this.query).subscribe((forums: ForumDeleteRequestDTOResponse[]) => {
        this.reportedForums = forums;
      })
    } else {
      this.getReportedForums();
    }
  }
}
