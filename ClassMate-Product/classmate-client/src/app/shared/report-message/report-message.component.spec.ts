import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportMessageComponent } from './report-message.component';

describe('ReportMessageComponent', () => {
  let component: ReportMessageComponent;
  let fixture: ComponentFixture<ReportMessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportMessageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
