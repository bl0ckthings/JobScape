import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationReminderForm } from './application-reminder-form';

describe('ApplicationReminderForm', () => {
  let component: ApplicationReminderForm;
  let fixture: ComponentFixture<ApplicationReminderForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationReminderForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationReminderForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
