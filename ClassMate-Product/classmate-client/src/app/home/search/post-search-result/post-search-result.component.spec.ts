import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostSearchResultComponent } from './post-search-result.component';

describe('PostSearchResultComponent', () => {
  let component: PostSearchResultComponent;
  let fixture: ComponentFixture<PostSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostSearchResultComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
