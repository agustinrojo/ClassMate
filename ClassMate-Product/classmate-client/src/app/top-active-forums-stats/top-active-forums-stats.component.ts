import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../services/statistics.service';
import { ForumService } from '../services/forum.service';
import { TopForumStat } from '../services/dto/statistics/top-forum-stat.interface';


@Component({
  selector: 'app-top-active-forums-stats',
  templateUrl: './top-active-forums-stats.component.html',
  styleUrl: './top-active-forums-stats.component.css'
})
export class TopActiveForumsStatsComponent implements OnInit {
  topForums: TopForumStat[] = [];

  constructor(
    private statisticsService: StatisticsService,
    private forumService: ForumService
  ) {}

  ngOnInit(): void {
    this.fetchTopActiveForums();
  }

  fetchTopActiveForums(): void {
    this.statisticsService.getTopActiveForums().subscribe(
      (forumStats) => {
        const forumIds = forumStats.map((forum) => forum.forumId);

        this.forumService.getForumNamesByIds(forumIds).subscribe(
          (forumNames) => {
            this.topForums = forumStats.map((stat) => {
              const matchingForum = forumNames.find(
                (forum) => forum.id === stat.forumId
              );
              return {
                id: stat.forumId,
                title: matchingForum ? matchingForum.title : `Foro ${stat.forumId}`,
                totalActivity: stat.totalActivity,
              };
            });
          },
          (error) => {
            console.error('Error fetching forum names', error);
          }
        );
      },
      (error) => {
        console.error('Error fetching top active forums', error);
      }
    );
  }
}
