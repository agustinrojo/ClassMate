import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserDropdownService {

  private openDropdownSubject = new Subject<number>();
  openDropdown$ = this.openDropdownSubject.asObservable();

  // Notify which dropdown is open (by its ID or index)
  notifyOpenDropdown(dropdownId: number): void {
    this.openDropdownSubject.next(dropdownId);
  }
}
