import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentReportsComponent } from './comment-reports.component';

describe('CommentReportsComponent', () => {
  let component: CommentReportsComponent;
  let fixture: ComponentFixture<CommentReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommentReportsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
