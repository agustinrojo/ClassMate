import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../services/statistics.service';

@Component({
  selector: 'app-users-activity-stats',
  templateUrl: './users-activity-stats.component.html',
  styleUrls: ['./users-activity-stats.component.css']
})
export class UsersActivityStatsComponent implements OnInit {
  totalUsers: number = 0;
  activeUsers: number = 0;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchStatistics();
  }

  fetchStatistics(): void {
    // Fetch total users
    this.statisticsService.getTotalUsersCreated().subscribe(
      (totalUsers) => {
        this.totalUsers = totalUsers;
      },
      (error) => {
        console.error('Error fetching total users count', error);
        this.totalUsers = 0;
      }
    );

    // Fetch active users
    this.statisticsService.getActiveUsers().subscribe(
      (activeUsers) => {
        this.activeUsers = activeUsers;
      },
      (error) => {
        console.error('Error fetching active users count', error);
        this.activeUsers = 0;
      }
    );
  }
}
