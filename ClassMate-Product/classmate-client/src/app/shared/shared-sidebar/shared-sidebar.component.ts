import { Component, OnInit } from '@angular/core';
import { ForumDataSidebar } from '../../services/dto/forum/forum-data-dto.interface';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../auth/auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumApiResponseDTO } from '../../services/dto/forum/forum-api-response-dto.interface';
import { ForumStateService } from '../../services/dto/state-services/forum-state.service';
import { ForumData } from '../../home/interfaces/forum-data.interface';

@Component({
  selector: 'app-shared-sidebar',
  templateUrl: './shared-sidebar.component.html',
  styleUrl: './shared-sidebar.component.css'
})
export class SharedSidebarComponent implements OnInit {
  public forums: ForumDataSidebar[] = [];
  public sections: { [key: string]: boolean } = {
    forums: true,
  };

  constructor(
    private _router: Router,
    private _authService: AuthServiceService,
    private _forumsService: ForumService,
    private _forumStateService: ForumStateService
  ) {}

  ngOnInit(): void {
    this.loadSubscribedForums();
    this.listenForumCreationEvent();
    this.listenForumRemovalEvent();
  }

  private listenForumCreationEvent(): void {
    this._forumStateService.getForumCreationEvent().subscribe((f: ForumDataSidebar | null) => {
      if (f) {
        this.forums.push(f!);
        this.sortForums();
      }
    } );
  }

  private listenForumRemovalEvent(): void {
    this._forumStateService.getForumRemovalEvent().subscribe((forumId: number | null) => {
      if (forumId) {
        this.forums = this.forums.filter(forum => forum.id !== forumId);
        this.sortForums();
      }
    } );
  }

  private loadSubscribedForums(): void {
    const forumsSubscribed: number[] = this._authService.getForumsSubscibed();

    if (forumsSubscribed && forumsSubscribed.length > 0) {
      let loadedForums: ForumDataSidebar[] = [];
      forumsSubscribed.forEach(forumId => {
        this._forumsService.getForumDataSidebarById(forumId.toString()).subscribe({
          next: (forum: ForumDataSidebar) => {
            if (forum && forum.id && forum.title) { // Check if the forum exists
              loadedForums.push(forum);
            } else{
              console.warn(`Forum with ID ${forumId} not found.`);
            }

          },
          error: (err) => {
            console.error(`Error loading forum with ID ${forumId}`, err);
          },
          complete: () => {
            this.forums = loadedForums;
            this.sortForums(); // Ensure consistent order
          }
        });
      });
    } else {
      console.warn('No subscribed forums found or user not logged in.');
    }
  }

  public toggleSection(section: string): void {
    this.sections[section] = !this.sections[section];
  }

  public navigateToForums(): void {
    this._router.navigate(['forums']);
  }

  public navigateToForum(forumId: number): void {
    this._router.navigate([`forum/${forumId}`]);
  }

  private sortForums(): void {
    this.forums.sort((a, b) => a.title.localeCompare(b.title));
  }
}
