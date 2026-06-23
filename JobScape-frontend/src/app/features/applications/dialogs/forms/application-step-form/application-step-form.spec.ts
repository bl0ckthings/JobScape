import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationStepForm } from './application-step-form';

describe('ApplicationStepForm', () => {
  let component: ApplicationStepForm;
  let fixture: ComponentFixture<ApplicationStepForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationStepForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationStepForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
