import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedSidebarComponent } from './shared-sidebar.component';

describe('SharedSidebarComponent', () => {
  let component: SharedSidebarComponent;
  let fixture: ComponentFixture<SharedSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SharedSidebarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SharedSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
