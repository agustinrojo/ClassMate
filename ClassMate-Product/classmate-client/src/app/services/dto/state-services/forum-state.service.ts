import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ForumData } from '../../../home/interfaces/forum-data.interface';


@Injectable({providedIn: 'root'})
export class ForumStateService {
  private forumDataSubject = new BehaviorSubject<ForumData | null>(null);
  private forumData$ = this.forumDataSubject.asObservable();

  constructor() { }

  public setForumData(forumData: ForumData){
    this.forumDataSubject.next(forumData);
  }

  public getForumData(): Observable<ForumData | null>{
    return this.forumData$;
  }

}
