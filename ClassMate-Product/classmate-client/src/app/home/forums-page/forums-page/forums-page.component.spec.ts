import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForumsPageComponent } from './forums-page.component';

describe('ForumsPageComponent', () => {
  let component: ForumsPageComponent;
  let fixture: ComponentFixture<ForumsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForumsPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForumsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
