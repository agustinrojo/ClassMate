import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-date-marker',
  templateUrl: './date-marker.component.html',
  styleUrl: './date-marker.component.css'
})
export class DateMarkerComponent implements OnInit{
  @Input() public date!: Date;
  public dateMarker: string = "default";

  ngOnInit(): void {
    this.calculateDateMarker();
  }

  public calculateDateMarker(){
    	if(this.isToday()){
        this.dateMarker = "HOY";
      } else if(this.isYesterday()) {
        this.dateMarker = "AYER";
      } else {
        this.dateMarker = this.formatDate();
      }
  }

  private isToday(): boolean {
    const today = new Date();
    const currentDate = new Date(this.date);

    today.setHours(0, 0, 0, 0);
    currentDate.setHours(0, 0, 0, 0);

    return today.getTime() === currentDate.getTime();
  }

  private isYesterday(): boolean {
    const yesterday = new Date();
    const currentDate = new Date(this.date);
    yesterday.setDate(yesterday.getDate() - 1);

    yesterday.setHours(0, 0, 0, 0);
    currentDate.setHours(0, 0, 0, 0);

    return currentDate.getTime() === yesterday.getTime();
  }

  private formatDate(): string {
    let currentDate = new Date(this.date);
    const day = String(currentDate.getDate()).padStart(2, '0');
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const year = currentDate.getFullYear();

    return `${day}/${month}/${year}`;
  }
}
