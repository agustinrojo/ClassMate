import { Component, Input, OnInit } from '@angular/core';
import { ChatUserDTO } from '../../../services/dto/chat/chat-user/chat-user-dto.interface';
import { UserProfileSearchDTO } from '../../../services/dto/user-profile/user-profile-search-dto.interface';

@Component({
  selector: 'app-chat-user',
  templateUrl: './chat-user.component.html',
  styleUrl: './chat-user.component.css'
})
export class ChatUserComponent implements OnInit{

  @Input() public user!: UserProfileSearchDTO;
  @Input() public isSelected!: boolean;
  // public userProfilePhoto!: string;

  ngOnInit(): void {
    // console.log(this.user.profilePhoto);
    // this.userProfilePhoto = URL.createObjectURL(this.user.profilePhoto);
  }
}
