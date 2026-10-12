import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusColumn } from './status-column';

describe('StatusColumn', () => {
  let component: StatusColumn;
  let fixture: ComponentFixture<StatusColumn>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusColumn],
    }).compileComponents();

    fixture = TestBed.createComponent(StatusColumn);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
