import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopActiveForumsStatsComponent } from './top-active-forums-stats.component';

describe('TopActiveForumsStatsComponent', () => {
  let component: TopActiveForumsStatsComponent;
  let fixture: ComponentFixture<TopActiveForumsStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TopActiveForumsStatsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TopActiveForumsStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
