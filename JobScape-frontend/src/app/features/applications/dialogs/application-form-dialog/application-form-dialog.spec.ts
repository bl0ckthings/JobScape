import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of, throwError } from 'rxjs';

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

    const select: HTMLButtonElement = fixture.nativeElement.querySelector('#initialStatus');
    select.click();
    fixture.detectChanges();

    const appliedOption = Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('[role="option"]'))
      .find(option => option.textContent?.trim() === 'CV envoyé') as HTMLButtonElement;
    appliedOption.click();
    fixture.detectChanges();

    expect(fixture.componentInstance.form.controls.initialStatus.value).toBe('APPLIED');
    expect(select.textContent).toContain('CV envoyé');
  });

  it('updates the other three form values through the styled dropdowns', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: { enabled: false, search: () => of([]) } },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ApplicationFormDialog);
    fixture.detectChanges();

    for (const [id, label] of [['country', 'Belgique'], ['source', 'LinkedIn'], ['workMode', 'Hybride']]) {
      (fixture.nativeElement.querySelector(`#${id}`) as HTMLButtonElement).click();
      fixture.detectChanges();
      const option = Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('[role="option"]'))
        .find(element => element.textContent?.trim() === label) as HTMLButtonElement;
      option.click();
      fixture.detectChanges();
    }

    expect(fixture.componentInstance.form.controls.country.value).toBe('Belgique');
    expect(fixture.componentInstance.form.controls.source.value).toBe('LinkedIn');
    expect(fixture.componentInstance.form.controls.workMode.value).toBe('Hybride');
  });

  it('closes a dropdown with Escape without changing its value', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: { enabled: false, search: () => of([]) } },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(ApplicationFormDialog);
    fixture.detectChanges();
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('#source');
    button.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[role="listbox"]')).toBeTruthy();

    button.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape', bubbles: true }));
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('[role="listbox"]')).toBeNull();
    expect(fixture.componentInstance.form.controls.source.value).toBe('');
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
    expect(submitted?.companyLogoUrl).toBeNull();
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

  it('stops showing the loading message when Brandfetch fails', async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationFormDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: null },
        { provide: BrandfetchSearchService, useValue: {
          enabled: true,
          search: () => throwError(() => new Error('Brandfetch unavailable')),
        } },
      ],
    }).compileComponents();

    const dialog = TestBed.createComponent(ApplicationFormDialog).componentInstance;
    dialog.form.controls.companyName.setValue('Acme');
    await new Promise(resolve => setTimeout(resolve, 300));

    expect(dialog.searching()).toBe(false);
    expect(dialog.searchFailed()).toBe(true);
  });
});
