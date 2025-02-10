import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../services/statistics.service';
import { ActivityResponseDTO } from '../services/dto/statistics/activity-response-dto.interface';
import moment from 'moment';
import 'moment/locale/es'; // Import Spanish locale

@Component({
  selector: 'app-forum-activity-chart',
  templateUrl: './forum-activity-chart.component.html',
  styleUrl: './forum-activity-chart.component.css',
})
export class ForumActivityChartComponent implements OnInit {
  chartData: any;
  chartOptions: any;
  currentMonth: moment.Moment = moment().locale('es'); // Set default locale to Spanish
  headerText: string = ''; // Header displaying month and year

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.loadDataForCurrentMonth();
  }

  /**
   * Load data for the current month and initialize the chart.
   */
  loadDataForCurrentMonth(): void {
    const startDate = this.currentMonth.startOf('month').toISOString();
    const endDate = this.currentMonth.endOf('month').subtract(3, 'hours').subtract(1, 'second').toISOString();
    console.log('Start date', startDate);
    console.log('End date', endDate);

    // Capitalize the first letter of the month
    const formattedMonth = this.currentMonth.format('MMMM YYYY');
    this.headerText =
      formattedMonth.charAt(0).toUpperCase() + formattedMonth.slice(1);

    this.statisticsService.getAggregatedActivity(startDate, endDate).subscribe(
      (data) => this.initializeChart(data),
      (error) => {
        console.error('Error fetching data:', error);
        this.initializeChart([]); // Gracefully handle API errors
      }
    );
  }

  /**
   * Navigate to the previous or next month.
   * @param offset Number of months to offset (negative for previous, positive for next)
   */
  changeMonth(offset: number): void {
    this.currentMonth.add(offset, 'months');
    this.loadDataForCurrentMonth();
  }

  /**
   * Initialize the chart with daily data and update axis labels.
   * @param data API response data
   */
  initializeChart(data: ActivityResponseDTO[]): void {
    const daysInMonth = this.currentMonth.daysInMonth();
    const labels = this.generateDayLabels(daysInMonth); // Generate labels for all days in the month
    const dailyCounts = this.aggregateDailyData(data, daysInMonth);

    this.chartData = {
      labels: labels, // X-axis: All days in DD format
      datasets: [
        {
          label: 'Actividad en Foros', // Change legend label
          data: dailyCounts, // Activity data for each day
          borderColor: 'rgba(75, 192, 192, 1)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          fill: true,
          tension: 0.4,
        },
      ],
    };

    this.chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
        },
      },
      scales: {
        x: {
          title: {
            display: true,
            text: 'Día', // Change X-axis title
          },
          ticks: {
            autoSkip: false, // Ensure all labels are displayed
            maxRotation: 0, // Prevent label rotation
            minRotation: 0,
          },
        },
        y: {
          title: {
            display: true,
            text: 'Posts + Comentarios', // Change Y-axis title
          },
          beginAtZero: true,
        },
      },
    };
  }


  /**
   * Generate day labels in DD format for the current month.
   * @param daysInMonth Total number of days in the current month
   * @returns An array of day labels (e.g., ["01", "02", ..., "28"])
   */
  generateDayLabels(daysInMonth: number): string[] {
    return Array.from({ length: daysInMonth }, (_, i) =>
      moment(this.currentMonth).date(i + 1).format('DD')
    );
  }

  /**
   * Aggregate activity data into daily counts for the current month.
   * @param data API response data
   * @param daysInMonth Total number of days in the current month
   * @returns An array of activity counts for each day
   */
  aggregateDailyData(data: ActivityResponseDTO[], daysInMonth: number): number[] {
    const dailyCounts = Array(daysInMonth).fill(0);

    data.forEach((entry) => {
      const dayOfMonth = moment(entry.timeBucket).date();
      dailyCounts[dayOfMonth - 1] += entry.totalActivity; // Aggregate activity by day
    });

    return dailyCounts;
  }
}
