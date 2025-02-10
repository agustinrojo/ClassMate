import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostReportsComponent } from './post-reports.component';

describe('PostReportsComponent', () => {
  let component: PostReportsComponent;
  let fixture: ComponentFixture<PostReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostReportsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
