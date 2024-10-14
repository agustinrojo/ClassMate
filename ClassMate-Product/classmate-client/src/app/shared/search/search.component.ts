import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ForumDTO } from '../../services/dto/forum/forum-dto.interface';
import { PostResponseDTO } from '../../services/dto/post/post-response-dto.interface';
import { ActivatedRoute, NavigationEnd, NavigationStart, Router } from '@angular/router';
import { ForumService } from '../../services/forum.service';
import { ForumStateService } from '../../services/dto/state-services/forum-state.service';
import { CurrentForumData } from '../../home/interfaces/current-forum-data.interface';
import { filter, Subscription } from 'rxjs';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, AfterViewInit, OnDestroy {
  public searchQuery: string = '';
  public searchPlaceholder: string = 'Buscar...';
  public isPostSearch: boolean = false;
  public searchResults: (ForumDTO | PostResponseDTO)[] = [];
  public showResults: boolean = false;
  public currentForum: CurrentForumData | null = null;
  private navigationSubscription!: Subscription;
  private isSearchPage: boolean = true;

  @ViewChild('forumTag') forumTag!: ElementRef;
  @ViewChild('searchInput') searchInput!: ElementRef;

  constructor(
    private _router: Router,
    private _forumService: ForumService,
    private _forumStateService: ForumStateService,
    private _elementRef: ElementRef,
    private _cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this._forumStateService.getCurrentForumData().subscribe((currentForumData: CurrentForumData | null) => {
      this.updateForumContext(currentForumData);
    });

    this.navigationSubscription = this._router.events.pipe(
    ).subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isSearchPage = event.urlAfterRedirects.includes('/search');
        this.checkAndResetForumContext(event.urlAfterRedirects);
      }
    });
  }

  private updateForumContext(currentForumData: CurrentForumData | null): void {
    this.currentForum = currentForumData;
    this.isPostSearch = !!currentForumData;
    this.searchPlaceholder = currentForumData ? `Buscar en /${currentForumData.title}` : 'Buscar Foros...';
    this.adjustPadding();
  }

  private checkAndResetForumContext(currentUrl: string): void {
    const isSameForum = this.currentForum && currentUrl.includes(`forum/${this.currentForum.id}`);

    if (this.currentForum && !isSameForum) {
      this.resetForumContext();
    } else {
      this.resetSearch();
      this._forumStateService.getCurrentForumData().subscribe((currentForumData: CurrentForumData | null) => {
        this.currentForum = currentForumData;
        this.isPostSearch = !!currentForumData;
        this.searchPlaceholder = currentForumData ? `Buscar en /${currentForumData.title}` : 'Buscar Foros...';
        this.adjustPadding();
      });
    }
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
    this.adjustPadding();
  }

  onSearchChange(eventEntity: Event): void {
    if (!this.isPostSearch) {
      const target = eventEntity.target as HTMLInputElement;
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
      this._router.navigate([`forum/${this.currentForum.id}/posts/search`], { queryParams: { query: this.searchQuery, forumId: this.currentForum.id } });
    } else {
      this._router.navigate(['forums/search'], { queryParams: { query: this.searchQuery } });
    }
  }

  onResultClick(id: number): void {
    if (!this.isPostSearch) {
      this._router.navigate([`forum/${id}`]);
      this.adjustPadding();
    }
  }

  isForumDTO(result: any): result is ForumDTO {
    return (result as ForumDTO).members !== undefined;
  }

  getMemberCountText(forum: ForumDTO): string {
    const count = forum.members.length;
    return count === 1 ? `${count} miembro` : `${count} miembros`;
  }

  private resetSearch(): void {
    if (!this.isSearchPage) this.searchQuery = '';
    this.showResults = false;
  }

  @HostListener('document:click', ['$event'])

  onDocumentClick(event: Event): void {
    if (!this._elementRef?.nativeElement.contains(event.target)) {
      this.showResults = false;
    }
  }

  resetForumContext(): void {
    this._forumStateService.setCurrentForumData(null);
    this.currentForum = null;
    this.isPostSearch = false;
    if (!this.isSearchPage) this.searchPlaceholder = 'Buscar Foros...';
    this.adjustPadding(true);
    this.resetSearch();
  }

  adjustPadding(reset: boolean = false): void {
    if (reset || !this.currentForum) {
      this.searchInput?.nativeElement.style.setProperty('--padding-left', '20px');
      this.searchInput?.nativeElement.classList.remove('has-tag');
    } else if (this.forumTag) {
      this.calculateTagWidth();
      this.searchInput?.nativeElement.classList.add('has-tag');
    }
    this._cdr.detectChanges(); // Ensure the view is updated with the new padding
  }

  private calculateTagWidth(): void {
    const tagWidth = this.forumTag.nativeElement.offsetWidth;
    this.searchInput.nativeElement.style.setProperty('--padding-left', `${tagWidth + 18}px`);
  }
}
