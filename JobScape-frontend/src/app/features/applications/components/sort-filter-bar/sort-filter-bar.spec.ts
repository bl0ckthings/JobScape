import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortFilterBar } from './sort-filter-bar';

describe('SortFilterBar', () => {
  let component: SortFilterBar;
  let fixture: ComponentFixture<SortFilterBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SortFilterBar],
    }).compileComponents();

    fixture = TestBed.createComponent(SortFilterBar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
