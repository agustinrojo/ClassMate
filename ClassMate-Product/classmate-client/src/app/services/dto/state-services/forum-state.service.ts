import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ForumData } from '../../../home/interfaces/forum-data.interface';
import { CurrentForumData } from '../../../home/interfaces/current-forum-data.interface';


@Injectable({providedIn: 'root'})
export class ForumStateService {
  private forumDataSubject = new BehaviorSubject<ForumData | null>(null);
  private forumData$ = this.forumDataSubject.asObservable();
  private currentForumDataSubject = new BehaviorSubject<CurrentForumData | null>(null);
  private currentForumData$ = this.currentForumDataSubject.asObservable();


  constructor() { }

  public setForumData(forumData: ForumData){
    this.forumDataSubject.next(forumData);
  }

  public getForumData(): Observable<ForumData | null>{
    return this.forumData$;
  }

  public setCurrentForumData(currentForumData: CurrentForumData | null){
    this.currentForumDataSubject.next(currentForumData);
  }

  public getCurrentForumData(): Observable<CurrentForumData | null>{
    return this.currentForumData$;
  }

}
