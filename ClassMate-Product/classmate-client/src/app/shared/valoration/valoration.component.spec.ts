import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValorationComponent } from './valoration.component';

describe('ValorationComponent', () => {
  let component: ValorationComponent;
  let fixture: ComponentFixture<ValorationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ValorationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValorationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
