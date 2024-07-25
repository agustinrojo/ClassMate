import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { ForumDTO } from '../../services/dto/forum/forum-dto.interface';
import { PostResponseDTO } from '../../services/dto/post/post-response-dto.interface';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ForumService } from '../../services/forum.service';
import { ForumStateService } from '../../services/dto/state-services/forum-state.service';
import { CurrentForumData } from '../../home/interfaces/current-forum-data.interface';
import { filter } from 'rxjs';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  public searchQuery: string = '';
  public searchPlaceholder: string = 'Buscar...';
  public isPostSearch: boolean = false;
  public searchResults: (ForumDTO | PostResponseDTO)[] = [];
  public showResults: boolean = false;
  public forumId: number | null = null;


  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _forumService: ForumService,
    private _forumStateService: ForumStateService,
    private _elementRef: ElementRef
  ) { }


  ngOnInit(): void {
    this._forumStateService.getCurrentForumData().subscribe((currentForumData: CurrentForumData | null) => {
      if (currentForumData != null) {
        this.searchPlaceholder = `Buscar en /${currentForumData.title}`;
        this.isPostSearch = true;
        this.forumId = currentForumData.id;
      } else {
        this.searchPlaceholder = 'Buscar Foros...';
        this.isPostSearch = false;
        this.forumId = null;
      }
    });

    // Clear the search query after navigation if navigating away from the search results page
    this._router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      if (!this._route.snapshot.queryParams['query']) {
        this.resetSearch();  // Clear the search query only if not returning to the search results page
      }
    });
  }

  onSearchChange(event: Event): void {
    if (!this.isPostSearch) {
      const target = event.target as HTMLInputElement;
      const query = target ? target.value : '';
      this.searchQuery = query;

      if (this.searchQuery.length > 2) {
        this._forumService.getForumsByTitle(this.searchQuery, 0, 10).subscribe(results => {
          this.searchResults = results;
          this.showResults = true;
        });
      } else {
        this.showResults = false;
      }
    }
  }

  onEnter(): void {
    if (this.isPostSearch && this.forumId) {
      this._router.navigate(['posts/search'], { queryParams: { query: this.searchQuery, forumId: this.forumId } });
    } else {
      this._router.navigate(['forums/search'], { queryParams: { query: this.searchQuery } });
    }
  }

  onResultClick(id: number): void {
    if (!this.isPostSearch) {
      this._router.navigate([`forum/${id}`]);
    }
  }

  isForumDTO(result: any): result is ForumDTO {
    return (result as ForumDTO).memberIds !== undefined;
  }

  getMemberCountText(forum: ForumDTO): string {
    const count = forum.memberIds.length;
    return count === 1 ? `${count} miembro` : `${count} miembros`;
  }

  private resetSearch(): void {
    this.searchQuery = '';
    this.showResults = false;
  }


  @HostListener('document:click', ['$event']) // Carlitos
  onDocumentClick(event: Event): void {
    if (!this._elementRef.nativeElement.contains(event.target)) {
      this.showResults = false;
    }
  }
}

