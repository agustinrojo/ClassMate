import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ForumData } from '../../../home/interfaces/forum-data.interface';
import { CurrentForumData } from '../../../home/interfaces/current-forum-data.interface';
import { ForumDTO } from '../forum/forum-dto.interface';
import { ForumDataSidebar } from '../forum/forum-data-dto.interface';


@Injectable({providedIn: 'root'})
export class ForumStateService {
  private forumDataSubject = new BehaviorSubject<ForumData | null>(null);
  private forumData$ = this.forumDataSubject.asObservable();
  private currentForumDataSubject = new BehaviorSubject<CurrentForumData | null>(null);
  private currentForumData$ = this.currentForumDataSubject.asObservable();
  private forumCreationSubject = new BehaviorSubject<ForumDataSidebar | null>(null);
  private forumCreation$ = this.forumCreationSubject.asObservable();
  private forumRemovalSubject = new BehaviorSubject<number | null>(null);
  private forumRemoval$ = this.forumRemovalSubject.asObservable();

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

  public setForumCreationEvent(forumCreationData: ForumDataSidebar | null) {
    this.forumCreationSubject.next(forumCreationData);
  }

  public getForumCreationEvent(): Observable<ForumDataSidebar | null> {
    return this.forumCreation$;
  }

  public setForumRemovalEvent(forumId: number | null) {
    this.forumRemovalSubject.next(forumId);
  }

  public getForumRemovalEvent(): Observable<number | null> {
    return this.forumRemoval$;
  }

}
