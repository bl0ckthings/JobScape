import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationStepsTab } from './application-steps-tab';

describe('ApplicationStepsTab', () => {
  let component: ApplicationStepsTab;
  let fixture: ComponentFixture<ApplicationStepsTab>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationStepsTab],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationStepsTab);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
