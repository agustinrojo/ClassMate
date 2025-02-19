import { Component, Input, OnInit, Renderer2, ElementRef, Output, EventEmitter } from '@angular/core';
import { UserProfileWithRoleDTO } from '../../../../services/dto/forum/user/user-profile-with-role-dto.interface';
import { UserProfileService } from '../../../../services/user-profile.service';
import { ForumService } from '../../../../services/forum.service';
import { UserDropdownService } from '../../../../services/user-dropdown.service';

@Component({
  selector: 'app-user-item',
  templateUrl: './user-item.component.html',
  styleUrls: ['./user-item.component.css']
})
export class UserItemComponent implements OnInit {
  @Input() public user!: UserProfileWithRoleDTO;
  @Input() public forumId!: number;
  @Input() public isModerator!: boolean;
  @Input() public currentUserId!: number;
  @Input() public forumCreatorId!: number;
  @Input() public isCreator!: boolean;  // Whether the current user is the forum creator
  @Input() public isAdmin!: boolean;    // Whether the current user is an admin/moderator

  @Output() public adminAddedEvent = new EventEmitter<number>();
  @Output() public adminRemovedEvent = new EventEmitter<number>();
  @Output() public userBannedEvent = new EventEmitter<number>();


  public userProfilePhotoUrl!: string;
  public isDropdownOpen: boolean = false;
  private dropdownId: number; // Add a unique identifier for this dropdown

  constructor(
    private _userProfileService: UserProfileService,
    private _forumService: ForumService,
    private dropdownService: UserDropdownService, // Inject the service
    private renderer: Renderer2,
    private el: ElementRef
  ) {
    this.dropdownId = Math.random(); // Generate a unique ID for each dropdown component
  }

  ngOnInit(): void {
    console.log(this.user)
    if (this.user.profilePhoto) {
      this.loadUserProfilePhoto(this.user.profilePhoto.photoId);
    }

    // Listen for other dropdowns being opened and close this one if needed
    this.dropdownService.openDropdown$.subscribe(openDropdownId => {
      if (openDropdownId !== this.dropdownId) {
        this.isDropdownOpen = false; // Close if another dropdown is opened
      }
    });

    // Close the dropdown if clicked outside
    this.renderer.listen('window', 'click', (event: Event) => {
      if (!this.el.nativeElement.contains(event.target)) {
        this.isDropdownOpen = false;
      }
    });
  }

  toggleDropdown(event: MouseEvent): void {
    event.stopPropagation(); // Prevent bubbling

    // Toggle the current dropdown and notify the service
    this.isDropdownOpen = !this.isDropdownOpen;

    if (this.isDropdownOpen) {
      // Notify other dropdowns to close
      this.dropdownService.notifyOpenDropdown(this.dropdownId);
    }
  }

  // Logic to determine if the "Banear Usuario" button should be enabled or disabled
  canBanUser(): boolean {
    if (this.isCreator) {
      return true; // Creator can ban anyone
    }

    if (this.isAdmin && this.user.userType !== 'Admin') {
      return true; // Moderators can ban subscribers, but not other admins
    }

    return false;
  }

  shouldShowBanButton(): boolean {
    // 1. Hide if I'm clicking myself
    if (this.user.userId === this.currentUserId) {
      return false;
    }

    // 2. Creator can ban anyone
    if (this.isCreator) {
      return true;
    }

    // 3. Moderator (Admin) can ban only Subscribers
    if (this.isAdmin && this.user.userType === 'Subscriber') {
      return true;
    }

    // 4. Moderator should not see the button for Creator or other Moderators
    return false;
  }

  // Method to ban a user
  banUser(): void {
    if (this.canBanUser()) {
      this._forumService.banUser(this.forumId, this.currentUserId, this.user.userId).subscribe({
        next: () => {
          console.log(`User ${this.user.userId} banned successfully.`);
          this.userBannedEvent.emit(this.user.userId); // Emit event to parent
        },
        error: (err) => {
          console.error('Failed to ban user:', err);
          console.log(err)
        }
      });
    }
  }

  getUserTypeLabel(userType: string): string {
    switch (userType) {
      case 'Creator':
        return 'Creador';
      case 'Admin':
        return 'Moderador';
      case 'Subscriber':
      default:
        return 'Suscriptor';
    }
  }

  public addAdmin(){
    let userId: number = this.user.userId;
    this._forumService.addAdmin(this.forumId, userId).subscribe(() => {
      this.adminAddedEvent.emit(userId);
    },
    err => {
      console.log(err);
    })
  }

  public removeAdmin(){
    let userId: number = this.user.userId;
    this._forumService.removeAdmin(this.forumId, userId).subscribe(() => {
      this.adminRemovedEvent.emit(userId);
    },
    err => {
      console.log(err);
    })
  }

  private loadUserProfilePhoto(photoId: number) {
    this._userProfileService.getUserProfilePhoto(photoId).subscribe((resp: Blob) => {
      this.userProfilePhotoUrl = URL.createObjectURL(resp);
    }, err => {
      console.error('Failed to load user photo:', err);
    });
  }
}
