import { Component, OnInit } from '@angular/core';
import { PostDeleteRequestDTOResponse } from '../../../services/dto/post/post-delete-request-dto-response.interface';
import { PostResponseDTO } from '../../../services/dto/post/post-response-dto.interface';
import { PostService } from '../../../services/post.service';
import { DeleteRequestDTOResponse } from '../../../services/dto/delete-request/delete-request-response.dto';

@Component({
  selector: 'app-post-reports',
  templateUrl: './post-reports.component.html',
  styleUrl: './post-reports.component.css'
})
export class PostReportsComponent implements OnInit{

  public reportedPosts!: PostDeleteRequestDTOResponse[];
  public reportedPostsDisplay: PostResponseDTO[] = [];
  public query: string = "";

  constructor(private _postService: PostService){}

  ngOnInit(): void {
    this.getReportedPosts();
  }

  private getReportedPosts() {
    this._postService.getReportedPosts().subscribe((posts: PostDeleteRequestDTOResponse[]) => {
      this.reportedPosts = posts;
      this.reportedPostsDisplay = [];
      posts.forEach((post: PostDeleteRequestDTOResponse) => {
        this.reportedPostsDisplay.push({
          id: post.id,
          creationDate: post.creationDate,
          commentCount: post.commentCount,
          title: post.title,
          body: post.body,
          author: post.author,
          valoration: post.valoration,
          likedByUser: false,
          reportedByUser: false,
          forumId: post.forumId,
          files: post.files,
          lastMilestone: 1000,
          hasBeenEdited: post.hasBeenEdited,
          dislikedByUser: false
        })
      })
    })
  }

  public getReportedPostsByKeyword() {
    if(this.query != ""){
      this.reportedPosts = []
      this._postService.getReportedPostsByKeyword(this.query).subscribe((posts: PostDeleteRequestDTOResponse[]) => {
        this.reportedPosts = posts;
        this.reportedPostsDisplay = [];
        posts.forEach((post: PostDeleteRequestDTOResponse) => {
          this.reportedPostsDisplay.push({
            id: post.id,
            creationDate: post.creationDate,
            commentCount: post.commentCount,
            title: post.title,
            body: post.body,
            author: post.author,
            valoration: post.valoration,
            likedByUser: false,
            reportedByUser: false,
            forumId: post.forumId,
            files: post.files,
            lastMilestone: 1000,
            hasBeenEdited: post.hasBeenEdited,
            dislikedByUser: false
          })
        })
      },
      (err) => {
        console.log(err)
      })
    } else {
      this.getReportedPosts();
    }
  }

  public absolvePost(postID: number){
    this.reportedPosts = [...this.reportedPosts.filter((post) => post.id != postID)];
    this.reportedPostsDisplay = [...this.reportedPostsDisplay.filter((post) => post.id != postID)];
  }

}
