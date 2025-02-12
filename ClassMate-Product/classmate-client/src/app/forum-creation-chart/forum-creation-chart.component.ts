import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ChartOptions } from 'chart.js';
import { ForumCreationMetricsDTO } from '../services/dto/statistics/forum-creation-metrics-dto.interface';
import { StatisticsService } from '../services/statistics.service'; // Import service to get data

@Component({
  selector: 'app-forum-creation-chart',
  templateUrl: './forum-creation-chart.component.html',
  styleUrls: ['./forum-creation-chart.component.css']
})
export class ForumCreationChartComponent implements OnChanges, OnInit {
  @Input() forumData: ForumCreationMetricsDTO[] = [];
  chartData: any;
  chartOptions: ChartOptions = {}; // Initialize chartOptions
  headerText: string = ''; // For displaying the year dynamically
  totalForums: number = 0; // For displaying total forums count
  currentYear: number = new Date().getFullYear(); // Set default to current year

  constructor(private statisticsService: StatisticsService) {}
  ngOnInit(): void {
    this.getTotalForumsCreated();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['forumData']) {
      this.initializeChart();
    }
  }

  /**
   * Fetch data for a specific year and update the chart.
   * @param year The year to fetch data for.
   */
  changeYear(year: number): void {
    this.currentYear = year;
    this.statisticsService.getMonthlyForumCreation(this.currentYear).subscribe(
      (data) => {
        this.forumData = data;
        this.initializeChart();
      },
      (error) => {
        console.error('Error fetching data for year', year, error);
        this.forumData = [];
        this.initializeChart();
      }
    );
  }

  private initializeChart() {
    const months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    const forumCounts = months.map((month, index) => {
      const monthData = this.forumData.find(d => new Date(d.yearMonth).getMonth() === index);
      return monthData ? monthData.forumCount : 0;
    });

    this.chartData = {
      labels: months,
      datasets: [
        {
          label: 'Foros creados',
          data: forumCounts,
          backgroundColor: 'rgba(75, 192, 192, 0.6)', // Light green
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1,
        },
      ],
    };

    this.chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          title: {
            display: true,
            text: 'Mes'
          },
        },
        y: {
          title: {
            display: true,
            text: 'Cantidad de Foros'
          },
          beginAtZero: true,
        },
      },
    };

    // Display the year in the header
    if (this.forumData.length > 0) {
      this.headerText = this.forumData[0].yearMonth.split('-')[0]; // Extract year from yearMonth
    } else {
      this.headerText = this.currentYear.toString();
    }
  }

  private getTotalForumsCreated() {
    this.statisticsService.getTotalForumsCreated().subscribe(
      (totalForums) => {
        this.totalForums = totalForums;
      },
      (error) => {
        console.error('Error fetching total forums count', error);
        this.totalForums = 0;
      }
    );
  }
}
