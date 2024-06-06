import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleForumPageComponent } from './single-forum-page.component';

describe('SingleForumPageComponent', () => {
  let component: SingleForumPageComponent;
  let fixture: ComponentFixture<SingleForumPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SingleForumPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SingleForumPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
