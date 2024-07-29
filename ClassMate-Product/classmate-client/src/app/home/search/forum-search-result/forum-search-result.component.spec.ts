import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForumSearchResultComponent } from './forum-search-result.component';

describe('ForumSearchResultComponent', () => {
  let component: ForumSearchResultComponent;
  let fixture: ComponentFixture<ForumSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForumSearchResultComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForumSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
