import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationDeleteDialog } from './application-delete-dialog';

describe('ApplicationDeleteDialog', () => {
  let component: ApplicationDeleteDialog;
  let fixture: ComponentFixture<ApplicationDeleteDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationDeleteDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationDeleteDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
