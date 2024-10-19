import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UserProfileWithRoleDTO } from '../../../services/dto/forum/user/user-profile-with-role-dto.interface';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-forum-users',
  templateUrl: './forum-users.component.html',
  styleUrl: './forum-users.component.css'
})
export class ForumUsersComponent implements OnChanges {
  @Input() forumId!: number;
  @Input() isModerator!: boolean;
  @Input() currentUserId!: number;
  @Input() forumCreatorId!: number;
  @Input() isCreator!: boolean;  // New input for creator status
  @Input() isAdmin!: boolean;    // New input for admin status

  allUsers: UserProfileWithRoleDTO[] = [];

  constructor(private _userService: UserService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['forumId'] && changes['forumId'].currentValue) {
      this.loadUsers();
    }
  }

  loadUsers() {
    this._userService.getUsersByForum(this.forumId).subscribe(users => {
      // Order users: first creator, then admins, then subscribers
      this.allUsers = [...users.filter(user => user.userType === 'Creator'),
                      ...users.filter(user => user.userType === 'Admin'),
                      ...users.filter(user => user.userType === 'Subscriber')];
    }, err => {
      console.error('Failed to load users:', err);
    });
  }
}
