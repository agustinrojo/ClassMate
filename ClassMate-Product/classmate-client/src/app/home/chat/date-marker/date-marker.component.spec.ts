import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DateMarkerComponent } from './date-marker.component';

describe('DateMarkerComponent', () => {
  let component: DateMarkerComponent;
  let fixture: ComponentFixture<DateMarkerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DateMarkerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DateMarkerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
