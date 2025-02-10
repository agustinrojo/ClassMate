import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivityResponseDTO } from './dto/statistics/activity-response-dto.interface';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  private baseUrl: string = 'http://localhost:8080/api/statistics'; // Replace with your backend URL

  constructor(private http: HttpClient) {}

  public getAggregatedActivity(
    startDate: string,
    endDate: string
  ): Observable<ActivityResponseDTO[]> {
    return this.http.get<ActivityResponseDTO[]>(
      `${this.baseUrl}/aggregatedForumsActivity?startDate=${startDate}&endDate=${endDate}`
    );
  }
}
