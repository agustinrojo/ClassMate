import { EventRequestDTO } from './dto/calendar/event-request-dto.interface';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { Observable } from 'rxjs';
import { EventResponseDTO } from './dto/calendar/event-response-dto.interface';
import { EventUpdateDTO } from './dto/calendar/event-update-dto.interface';

@Injectable({providedIn: 'root'})
export class CalendarService {
  private baseUrl: string = 'http://localhost:8080/api/eventEntities'
  private loggedUserId!: number;

  constructor(
    private _authService: AuthServiceService,
    private http: HttpClient
  ) {
    this.loggedUserId = this._authService.getUserId();
  }

  public getEventsByUserId(): Observable<EventResponseDTO[]> {
    return this.http.get<EventResponseDTO[]>(`${this.baseUrl}/userEvents/${this.loggedUserId}`);
  }

  public saveEvent(eventRequestDTO: EventRequestDTO): Observable<EventResponseDTO> {
    return this.http.post<EventResponseDTO>(`${this.baseUrl}`, eventRequestDTO);
  }

  public updateEvent( id: number, eventUpdateDTO: EventUpdateDTO ): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}`, eventUpdateDTO);
  }

  public deleteEvent( id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}?userId=${this.loggedUserId}`);
  }

}
