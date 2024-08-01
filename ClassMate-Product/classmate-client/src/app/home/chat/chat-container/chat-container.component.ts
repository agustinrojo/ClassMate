import { Component, OnInit } from '@angular/core';
import { UserProfileService } from '../../../services/user-profile.service';
import { UserProfileSearchDTO } from '../../../services/dto/user-profile/user-profile-search-dto.interface';

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrl: './chat-container.component.css'
})
export class ChatContainerComponent implements OnInit{
  public searchQuery: string = "";
  public searchResults: UserProfileSearchDTO[] = [];


  constructor(
    private _userProfileService:UserProfileService
  ){

  }

  ngOnInit(): void {
  }

  public onSearchChange($event: Event) {
    const target = $event.target as HTMLInputElement;
    this.searchQuery = target ? target.value : '';
    if(this.searchQuery.length > 2){
      this._userProfileService.searchChatUserByNickname(this.searchQuery).subscribe((resp: UserProfileSearchDTO[]) => {
        console.log(resp);
        this.searchResults = resp;
      })
    } else {
      this.searchResults = [];
    }
  }

}
