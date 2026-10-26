import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of } from 'rxjs';

import { ApplicationFormDialog, ApplicationFormValue } from './application-form-dialog';
import { BrandfetchSearchService } from '../../services/brandfetch-search.service';

describe('ApplicationFormDialog', () => {
  it('keeps the French status label but submits its API value', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: { enabled: true, search: () => of([]) } },
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
        { provide: BrandfetchSearchService, useValue: { enabled: true, search: () => of([]) } },
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

  it('shows Brandfetch suggestions and submits the selected domain', async () => {
    let submitted: ApplicationFormValue | undefined;
    const search = vi.fn(() => of([
      { brandId: '1', name: 'Acme', domain: 'acme.com', icon: 'https://example.com/acme.png' },
    ]));
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: (value: ApplicationFormValue) => submitted = value } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: { enabled: true, search } },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ApplicationFormDialog);
    fixture.componentInstance.form.controls.companyName.setValue('Acm');
    await new Promise(resolve => setTimeout(resolve, 300));
    fixture.detectChanges();

    expect(search).toHaveBeenCalledWith('Acm');
    const suggestion = fixture.nativeElement.querySelector('[data-testid="company-suggestion"]') as HTMLButtonElement;
    expect(suggestion.textContent).toContain('acme.com');
    suggestion.click();

    fixture.componentInstance.form.patchValue({
      jobTitle: 'Developer', city: 'Paris', source: 'LinkedIn', workMode: 'Remote',
    });
    fixture.componentInstance.submit();

    expect(submitted?.companyName).toBe('Acme');
    expect(submitted?.companyDomain).toBe('acme.com');
  });

  it('clears the selected domain when the user changes the company name', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: { enabled: true, search: () => of([]) } },
      ],
    }).compileComponents();

    const dialog = TestBed.createComponent(ApplicationFormDialog).componentInstance;
    dialog.selectCompany({ brandId: '1', name: 'Acme', domain: 'acme.com', icon: null });
    dialog.form.controls.companyName.setValue('Another company');

    expect(dialog.selectedCompany()).toBeNull();
  });
});
