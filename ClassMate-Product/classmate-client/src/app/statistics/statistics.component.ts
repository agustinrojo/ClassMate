import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../services/statistics.service';
import { ForumCreationMetricsDTO } from '../services/dto/statistics/forum-creation-metrics-dto.interface';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  forumData: ForumCreationMetricsDTO[] = [];

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.loadForumStats();
  }

  loadForumStats(): void {
    const year = 2025; // You can make this dynamic if needed
    this.statisticsService.getMonthlyForumCreation(year).subscribe(
      (data) => {
        this.forumData = data;
      },
      (error) => {
        console.error('Error loading forum creation stats:', error);
      }
    );
  }
}
