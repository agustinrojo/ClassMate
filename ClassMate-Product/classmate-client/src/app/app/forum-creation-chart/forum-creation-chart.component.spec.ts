import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForumCreationChartComponent } from './forum-creation-chart.component';

describe('ForumCreationChartComponent', () => {
  let component: ForumCreationChartComponent;
  let fixture: ComponentFixture<ForumCreationChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForumCreationChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForumCreationChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
