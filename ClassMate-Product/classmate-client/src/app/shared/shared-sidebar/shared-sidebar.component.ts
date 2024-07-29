import { Component, OnInit } from '@angular/core';
import { ForumData } from '../../services/dto/forum/forum-data-dto.interface';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../auth/auth-service.service';
import { ForumService } from '../../services/forum.service';
import { ForumApiResponseDTO } from '../../services/dto/forum/forum-api-response-dto.interface';

@Component({
  selector: 'app-shared-sidebar',
  templateUrl: './shared-sidebar.component.html',
  styleUrl: './shared-sidebar.component.css'
})
export class SharedSidebarComponent implements OnInit {
  public forums: ForumData[] = [];
  public sections: { [key: string]: boolean } = {
    forums: true,
  };

  constructor(
    private _router: Router,
    private _authService: AuthServiceService,
    private _forumsService: ForumService
  ) {}

  ngOnInit(): void {
    this.loadSubscribedForums();
    console.log("Llegamos");

  }

  private loadSubscribedForums(): void {
    const forumsSubscribed: number[] = this._authService.getForumsSubscibed();
    console.log('Subscribed forums IDs:', forumsSubscribed);
    if (forumsSubscribed && forumsSubscribed.length > 0) {
      forumsSubscribed.forEach(forumId => {
        this._forumsService.getForumById(forumId.toString()).subscribe({
          next: (forum: ForumApiResponseDTO) => {
            const forumData: ForumData = {
              id: forum.forum.id,
              title: forum.forum.title
            };
            this.forums.unshift(forumData);
            console.log('Loaded forum:', forumData);  // Log each forum loaded
          },
          error: (err) => {
            console.error(`Error loading forum with ID ${forumId}`, err);
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

  public navigateToForum(forumId: number): void {
    this._router.navigate([`forum/${forumId}`]);
  }
}
