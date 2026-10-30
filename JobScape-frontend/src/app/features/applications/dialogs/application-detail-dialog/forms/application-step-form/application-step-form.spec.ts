import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationStepForm } from './application-step-form';
import { DetailStep } from '../../application-detail.models';

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

  it('emits only the persisted step fields when creating a step', () => {
    const saved = vi.fn();
    component.saved.subscribe(saved);
    component.form.patchValue({
      status: 'FIRST_CONTACT',
      date: '2026-01-12',
      comment: 'Premier échange.',
      contactId: '7',
    });

    component.submit();

    expect(saved).toHaveBeenCalledExactlyOnceWith({
      status: 'FIRST_CONTACT',
      date: '2026-01-12',
      comment: 'Premier échange.',
      contactId: 7,
    });
  });

  it('loads and submits an existing step using its recorded status', async () => {
    const editedFixture = TestBed.createComponent(ApplicationStepForm);
    const step: DetailStep = {
      id: 1,
      status: 'APPLIED',
      date: '2026-01-08',
      comment: 'CV envoyé.',
      contactId: null,
    };
    editedFixture.componentRef.setInput('step', step);
    await editedFixture.whenStable();
    const edited = editedFixture.componentInstance;
    const saved = vi.fn();
    edited.saved.subscribe(saved);

    expect(edited.form.controls.status.value).toBe('APPLIED');
    edited.submit();
    expect(saved).toHaveBeenCalledExactlyOnceWith({
      status: 'APPLIED',
      date: '2026-01-08',
      comment: 'CV envoyé.',
      contactId: null,
    });
  });
});
