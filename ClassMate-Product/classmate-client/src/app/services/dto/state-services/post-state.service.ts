import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { PostData } from '../../../home/interfaces/post-data.interface';

@Injectable({providedIn: 'root'})
export class PostStateService {
  private postDataSubject = new BehaviorSubject<PostData | null>(null);
  private postData$ = this.postDataSubject.asObservable();
  constructor() { }

  public setPostData(data: PostData){
    this.postDataSubject.next(data)
  }

  public getPostData(): Observable<PostData | null> {
    return this.postData$;
  }

}
