import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { ApplicationFormDialog, ApplicationFormValue } from './application-form-dialog';

describe('ApplicationFormDialog', () => {
  it('keeps the French status label but submits its API value', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ApplicationFormDialog);
    fixture.detectChanges();

    const select = fixture.nativeElement.querySelector('#initialStatus') as HTMLSelectElement;
    const appliedOption = Array.from(select.options).find(option => option.value === 'APPLIED');

    expect(fixture.componentInstance.form.controls.initialStatus.value).toBe('APPLIED');
    expect(appliedOption?.textContent?.trim()).toBe('CV envoyé');
  });

  it('sends no date when the optional date field was cleared', async () => {
    let submitted: ApplicationFormValue | undefined;
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: (value: ApplicationFormValue) => submitted = value } },
        { provide: MAT_DIALOG_DATA, useValue: null },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ApplicationFormDialog);
    fixture.componentInstance.form.patchValue({
      companyName: 'JobScape',
      jobTitle: 'Developer',
      city: 'Paris',
      source: 'LinkedIn',
      workMode: 'Remote',
      initialDate: '',
    });

    fixture.componentInstance.submit();

    expect(submitted?.initialDate).toBeNull();
  });
});
