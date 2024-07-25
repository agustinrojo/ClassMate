import { AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
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
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, AfterViewInit {
  public searchQuery: string = '';
  public searchPlaceholder: string = 'Buscar...';
  public isPostSearch: boolean = false;
  public searchResults: (ForumDTO | PostResponseDTO)[] = [];
  public showResults: boolean = false;
  public currentForum: CurrentForumData | null = null;

  @ViewChild('forumTag') forumTag!: ElementRef;
  @ViewChild('searchInput') searchInput!: ElementRef;

  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _forumService: ForumService,
    private _forumStateService: ForumStateService,
    private _elementRef: ElementRef
  ) {}

  ngOnInit(): void {
    this._forumStateService.getCurrentForumData().subscribe((currentForumData: CurrentForumData | null) => {
      this.currentForum = currentForumData;
      this.isPostSearch = !!currentForumData;
      this.searchPlaceholder = currentForumData ? `Buscar en /${currentForumData.title}` : 'Buscar Foros...';
      this.adjustPadding(); // Adjust padding based on forum tag visibility
    });

    this._router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      if (!this._route.snapshot.queryParams['query']) {
        this.resetSearch();  // Clear the search query only if not returning to the search results page
      }
    });
  }

  ngAfterViewInit(): void {
    this.adjustPadding(); // Adjust padding on initial view load
  }

  onSearchChange(event: Event): void {
    if (!this.isPostSearch) {
      const target = event.target as HTMLInputElement;
      this.searchQuery = target ? target.value : '';

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
    if (this.isPostSearch && this.currentForum) {
      this._router.navigate(['posts/search'], { queryParams: { query: this.searchQuery, forumId: this.currentForum.id } });
    } else {
      this._router.navigate(['forums/search'], { queryParams: { query: this.searchQuery } });
    }
  }

  onResultClick(id: number): void {
    console.log(id);

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

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    if (!this._elementRef.nativeElement.contains(event.target)) {
      this.showResults = false;
    }
  }

  resetForumContext(): void {
    this._forumStateService.setCurrentForumData(null);
    this.currentForum = null;
    this.isPostSearch = false;
    this.searchPlaceholder = 'Buscar Foros...';
    this.adjustPadding(true);
    this.resetSearch();
  }

  adjustPadding(reset: boolean = false): void {
    if (this.searchInput) {
      if (reset) {
        this.searchInput.nativeElement.style.paddingLeft = '20px'; // Default padding-left value
      } else if (this.forumTag) {
        const tagWidth = this.forumTag.nativeElement.offsetWidth;
        this.searchInput.nativeElement.style.paddingLeft = `${tagWidth + 13}px`; // Adjust as needed
      }
    }
  }
}
