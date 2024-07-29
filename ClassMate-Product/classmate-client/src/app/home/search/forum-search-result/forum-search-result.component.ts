import { Component, OnInit } from '@angular/core';
import { ForumDTO } from '../../../services/dto/forum/forum-dto.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumService } from '../../../services/forum.service';
import { User } from '../../../auth/dto/user-dto.interface';

@Component({
  selector: 'app-forum-search-result',
  templateUrl: './forum-search-result.component.html',
  styleUrl: './forum-search-result.component.css'
})
export class ForumSearchResultComponent implements OnInit {
  public forums: ForumDTO[] = [];
  public query: string = '';
  public forumsSubscribed: number[] = [];
  public noForumsFound: boolean = false;
  public isForumSearchActive: boolean = true;

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _forumService: ForumService
  ) {}

  ngOnInit(): void {
    this._route.queryParams.subscribe(params => {
      this.query = params['query'] || '';
      this.searchForums();
    });
    this.getForumsSubscribed();
  }

  searchForums(): void {
    this._forumService.getForumsByTitle(this.query).subscribe(forums => {
      this.forums = forums;
      this.noForumsFound = this.forums.length === 0;
    });
  }

  navigateToForum(id: number): void {
    this._router.navigate([`forum/${id}`]);
  }

  navigateToForumSearch(): void {
    this.isForumSearchActive = true;
    this._router.navigate(['forums/search'], { queryParams: { query: this.query } });
  }

  switchToPostSearch(): void {
    this.isForumSearchActive = false;
    this._router.navigate(['posts/search'], { queryParams: { query: this.query } });
  }


  subscribe(forumId: number): void {
    this._forumService.addMember(forumId, 123).subscribe(() => { // Replace '123' with actual user ID logic
      this.forumsSubscribed.push(forumId);
    });
  }

  private getForumsSubscribed(){
    let user: User = JSON.parse(localStorage.getItem("user")!);
    this.forumsSubscribed = user.forumsSubscribed;
  }

  getMemberCountText(forum: ForumDTO): string {
    const count = forum.memberIds.length;
    return count === 1 ? `${count} miembro` : `${count} miembros`;
  }
}
