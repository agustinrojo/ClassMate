import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestPasswordResetComponent } from './request-password-reset.component';

describe('RequestPasswordResetComponent', () => {
  let component: RequestPasswordResetComponent;
  let fixture: ComponentFixture<RequestPasswordResetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequestPasswordResetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RequestPasswordResetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
