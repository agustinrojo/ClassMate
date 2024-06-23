import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileBadgeComponent } from './file-badge.component';

describe('FileBadgeComponent', () => {
  let component: FileBadgeComponent;
  let fixture: ComponentFixture<FileBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FileBadgeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FileBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
