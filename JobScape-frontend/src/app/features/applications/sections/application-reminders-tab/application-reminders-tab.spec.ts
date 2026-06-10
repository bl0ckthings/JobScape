import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationRemindersTab } from './application-reminders-tab';

describe('ApplicationRemindersTab', () => {
  let component: ApplicationRemindersTab;
  let fixture: ComponentFixture<ApplicationRemindersTab>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationRemindersTab],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationRemindersTab);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
