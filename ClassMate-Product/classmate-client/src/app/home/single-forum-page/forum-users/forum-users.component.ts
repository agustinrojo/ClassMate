import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { UserProfileWithRoleDTO } from '../../../services/dto/forum/user/user-profile-with-role-dto.interface';
import { UserService } from '../../../services/user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { debounceTime } from 'rxjs';
import { UserProfileService } from '../../../services/user-profile.service';

@Component({
  selector: 'app-forum-users',
  templateUrl: './forum-users.component.html',
  styleUrl: './forum-users.component.css'
})
export class ForumUsersComponent implements OnChanges, OnInit {
  @Input() forumId!: number;
  @Input() isModerator!: boolean;
  @Input() currentUserId!: number;
  @Input() forumCreatorId!: number;
  @Input() isCreator!: boolean;  // New input for creator status
  @Input() isAdmin!: boolean;    // New input for admin status

  allUsers: UserProfileWithRoleDTO[] = [];
  searchResultUsers: UserProfileWithRoleDTO[] = [];

  public searchForm!: FormGroup;
  public query: string = "";


  constructor(
    private _userService: UserService,
    private _userProfileService: UserProfileService,
    private _fb: FormBuilder) {}

  ngOnInit(): void {
    this.searchForm = this._fb.group({
      query: ["", [], []]
    })
    this.subscribeToSearchInputChanges()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['forumId'] && changes['forumId'].currentValue) {
      this.loadUsers();
    }
  }

  public adminAddedEventAllUsers(userId: number) {
    let userIndex = this.allUsers.findIndex((user: UserProfileWithRoleDTO) => user.userId == userId);
    let newAdmin = this.allUsers[userIndex];
    newAdmin.userType = "Admin";

    this.allUsers[userIndex] = newAdmin;
  }

  public adminAddedEventSearchResults(userId: number) {
    let userIndex = this.searchResultUsers.findIndex((user: UserProfileWithRoleDTO) => user.userId == userId);
    let newAdmin = this.searchResultUsers[userIndex];
    newAdmin.userType = "Admin";

    this.searchResultUsers[userIndex] = newAdmin;
    this.adminAddedEventAllUsers(userId);
  }

  public adminRemovedEventAllUsers(userId: number){
    let userIndex = this.allUsers.findIndex((user: UserProfileWithRoleDTO) => user.userId == userId);
    let newAdmin = this.allUsers[userIndex];
    newAdmin.userType = "Subscriber";

    this.allUsers[userIndex] = newAdmin;
  }

  public adminRemovedEventSearchResults(userId: number) {
    let userIndex = this.searchResultUsers.findIndex((user: UserProfileWithRoleDTO) => user.userId == userId);
    let newAdmin = this.searchResultUsers[userIndex];
    newAdmin.userType = "Subscriber";

    this.searchResultUsers[userIndex] = newAdmin;
    this.adminRemovedEventAllUsers(userId);
  }

  private loadUsers() {
    this._userService.getUsersByForum(this.forumId).subscribe(users => {
      // Order users: first creator, then admins, then subscribers
      this.allUsers = [...users.filter(user => user.userType === 'Creator'),
                      ...users.filter(user => user.userType === 'Admin'),
                      ...users.filter(user => user.userType === 'Subscriber')];
    }, err => {
      console.error('Failed to load users:', err);
    });
  }

  private subscribeToSearchInputChanges(){
    this.searchForm.valueChanges
    .pipe(
      debounceTime(200)
    ).subscribe((value) => {
      let query: string = value.query;
      this.query = query;
      if(query.length >= 1){
        this.searchUsers(this.forumId, query);
      } else {
        this.searchResultUsers = [];
      }
    })
  }

  private searchUsers(forumId: number, query: string){
    this._userProfileService.searchUsersInForum(forumId, query).subscribe((members: UserProfileWithRoleDTO[]) => {
      console.log(members)
      this.searchResultUsers = [...members];
    },
    err => {
      console.log(err);
    })
  }
}
