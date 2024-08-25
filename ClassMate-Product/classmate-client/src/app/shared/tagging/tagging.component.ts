import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserProfileResponseDTO } from '../../services/dto/user-profile/user-profile-response-dto.interface';
import { TaggingService } from '../../services/tagging.service';
import { debounceTime, of, switchMap } from 'rxjs';
import { ForumDTO } from '../../services/dto/forum/forum-dto.interface';

@Component({
  selector: 'app-tagging',
  templateUrl: './tagging.component.html',
  styleUrls: ['./tagging.component.css']
})
export class TaggingComponent implements OnInit {

  @Input() textAreaControl!: FormControl;
  @Output() contentChange = new EventEmitter<string>();
  public userSuggestions: UserProfileResponseDTO[] = [];
  public forumSuggestions: ForumDTO[] = [];
  public showSuggestions = false;
  private selectedIndex: number = 0;
  private loggedInUserId!: number;

  constructor(private _taggingService: TaggingService) {}

  ngOnInit(): void {
    const loggedUser = JSON.parse(localStorage.getItem('user')!);
    this.loggedInUserId = loggedUser.id;

    this.textAreaControl.valueChanges.pipe(
        debounceTime(300),
        switchMap(value => {
            // Reset suggestions if the input is empty
            if (!value || value.trim().length === 0) {
                this.showSuggestions = false;
                this.userSuggestions = [];
                this.forumSuggestions = [];
                return of([]);
            }
            return this.detectAndFetchSuggestions(value);
        })
    ).subscribe(() => {
        this.showSuggestions = (this.userSuggestions.length > 0 || this.forumSuggestions.length > 0) &&
            (this.textAreaControl.value.includes('p/') || this.textAreaControl.value.includes('f/'));
        this.selectedIndex = 0;
    });
}

  private detectAndFetchSuggestions(value: string) {
    const userTagQuery = this.extractTag(value, 'p/');
    const forumTagQuery = this.extractTag(value, 'f/');

    if (userTagQuery.length > 0) {
      return this._taggingService.searchUserByNickname(userTagQuery).pipe(
        switchMap(users => {
          this.userSuggestions = users.filter(user => user.userId !== this.loggedInUserId);
          this.forumSuggestions = [];
          return of(users);
        })
      );
    } else if (forumTagQuery.length > 0) {
      return this._taggingService.getForumsByTitle(forumTagQuery).pipe(
        switchMap(forums => {
          this.forumSuggestions = forums;
          this.userSuggestions = [];
          return of(forums);
        })
      );
    }

    return of([]); // Return an empty observable if no query is valid
  }


  private extractTag(value: string, prefix: string): string {
    const match = value.match(new RegExp(`${prefix}([a-zA-Z0-9-_]+)$`));
    return match ? match[1] : '';
  }


  public insertTag(tagName: string, id: number, tagType: 'user' | 'forum') {
    const value = this.textAreaControl.value;
    const newTag = tagType === 'user' ? `p/${tagName}:${id}` : `f/${tagName}:${id}`;
    const newValue = value.replace(/(p\/|f\/)([a-zA-Z][^}\s]*)$/, newTag);

    this.textAreaControl.setValue(newValue);
    this.showSuggestions = false;
    this.userSuggestions = [];
    this.forumSuggestions = [];
    this.contentChange.emit(newValue);

  }

  @HostListener('document:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent) {
    if (this.showSuggestions) {
      if (event.key === 'ArrowDown') {
        event.preventDefault();
        this.selectedIndex = (this.selectedIndex + 1) % (this.userSuggestions.length + this.forumSuggestions.length);
      } else if (event.key === 'ArrowUp') {
        event.preventDefault();
        this.selectedIndex = (this.selectedIndex - 1 + this.userSuggestions.length + this.forumSuggestions.length) % (this.userSuggestions.length + this.forumSuggestions.length);
      } else if (event.key === 'Enter' || event.key === 'Tab') {
        event.preventDefault();
        if (this.selectedIndex < this.userSuggestions.length) {
          const selectedSuggestion = this.userSuggestions[this.selectedIndex];
          this.insertTag(selectedSuggestion.nickname, selectedSuggestion.userId, 'user');
        } else {
          const selectedSuggestion = this.forumSuggestions[this.selectedIndex - this.userSuggestions.length];
          this.insertTag(selectedSuggestion.title, selectedSuggestion.id, 'forum');
        }
      }
    }
  }

  public isSelected(index: number): boolean {
    return index === this.selectedIndex;
  }
}
