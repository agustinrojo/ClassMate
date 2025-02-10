import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForumActivityChartComponent } from './forum-activity-chart.component';

describe('ForumActivityChartComponent', () => {
  let component: ForumActivityChartComponent;
  let fixture: ComponentFixture<ForumActivityChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForumActivityChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForumActivityChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
