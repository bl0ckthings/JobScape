import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseTextarea } from './base-textarea';

describe('BaseTextarea', () => {
  let component: BaseTextarea;
  let fixture: ComponentFixture<BaseTextarea>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaseTextarea],
    }).compileComponents();

    fixture = TestBed.createComponent(BaseTextarea);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
