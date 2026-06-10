import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationFormDialog } from './application-form-dialog';

describe('ApplicationFormDialog', () => {
  let component: ApplicationFormDialog;
  let fixture: ComponentFixture<ApplicationFormDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationFormDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
