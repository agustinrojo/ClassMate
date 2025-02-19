import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersActivityStatsComponent } from './users-activity-stats.component';

describe('UsersActivityStatsComponent', () => {
  let component: UsersActivityStatsComponent;
  let fixture: ComponentFixture<UsersActivityStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsersActivityStatsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsersActivityStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
