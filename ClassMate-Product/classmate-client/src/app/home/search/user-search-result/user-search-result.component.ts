import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { UserProfileService } from '../../../services/user-profile.service';
import { UserProfileResponseDTO } from '../../../services/dto/user-profile/user-profile-response-dto.interface';

@Component({
  selector: 'app-user-search-result',
  templateUrl: './user-search-result.component.html',
  styleUrl: './user-search-result.component.css'
})
export class UserSearchResultComponent implements OnInit{
  private query: string = '';
  public userProfiles: UserProfileResponseDTO[] = [];

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _userProfileService: UserProfileService
  ) {}

  ngOnInit(): void {
    this._route.queryParams.subscribe((params: Params) => {
      this.query = params['query'] || '';
    })
    this.getUserProfiles();
    console.log(this.query);
  }

  navigateToForumSearch(): void {
    this._router.navigate(['forums/search'], { queryParams: { query: this.query } });
  }

  switchToPostSearch(): void {
    this._router.navigate(['posts/search'], { queryParams: { query: this.query } });
  }

  private getUserProfiles(){
    this._userProfileService.searchUserByNickname(this.query).subscribe((users: UserProfileResponseDTO[]) => {
      console.log(users)
      this.userProfiles = [...users];
    })
  }

}
