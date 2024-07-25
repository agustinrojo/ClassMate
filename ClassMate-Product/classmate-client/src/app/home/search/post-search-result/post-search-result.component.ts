import { Component, OnInit } from '@angular/core';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';

@Component({
  selector: 'app-post-search-result',
  templateUrl: './post-search-result.component.html',
  styleUrl: './post-search-result.component.css'
})
export class PostSearchResultComponent implements OnInit {
  public posts: PostResponseDTO[] = [];
  public query: string = '';
  public forumId: number | null = null;

  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _postService: PostService
  ) {}

  ngOnInit(): void {
    this._route.queryParams.subscribe(params => {
      this.query = params['query'] || '';
      this.forumId = params['forumId'] ? +params['forumId'] : null;
      this.searchPosts();
    });
  }

  searchPosts(): void {
    if (this.forumId) {
      this._postService.getPostsByNameAndForumId(this.query, this.forumId).subscribe(posts => {
        this.posts = posts;
      });
    } else {
      this._postService.getPostsByName(this.query).subscribe(posts => {
        this.posts = posts;
      });
    }
  }

  navigateToPost(id: number): void {
    this._router.navigate([`post/${id}`]);
  }
}
