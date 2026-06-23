import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationContactForm } from './application-contact-form';

describe('ApplicationContactForm', () => {
  let component: ApplicationContactForm;
  let fixture: ComponentFixture<ApplicationContactForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationContactForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationContactForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
