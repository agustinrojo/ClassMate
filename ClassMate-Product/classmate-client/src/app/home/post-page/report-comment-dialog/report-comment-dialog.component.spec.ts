import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportCommentDialogComponent } from './report-comment-dialog.component';

describe('ReportCommentDialogComponent', () => {
  let component: ReportCommentDialogComponent;
  let fixture: ComponentFixture<ReportCommentDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportCommentDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportCommentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
