import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotificationPreferenceDTO } from './dto/notification/notification-preference-dto.interface copy';
import { NotificationPreferenceUpdateDTO } from './dto/notification/notification-preference-update-dto.interface copy 2';


@Injectable({
  providedIn: 'root'
})
export class NotificationPreferenceService {
  private baseUrl: string = "http://localhost:8090/api/preferences";

  constructor(private http: HttpClient) {}

  public getUserPreferences(userId: number): Observable<NotificationPreferenceDTO> {
    return this.http.get<NotificationPreferenceDTO>(`${this.baseUrl}/user/${userId}`);
  }

  public updateUserPreferences(userId: number, preferences: NotificationPreferenceUpdateDTO): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/user/${userId}`, preferences);
  }
}
