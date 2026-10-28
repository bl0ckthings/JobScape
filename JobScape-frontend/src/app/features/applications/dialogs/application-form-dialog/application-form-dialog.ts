import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { catchError, of, switchMap, tap, timer } from 'rxjs';
import { ApplicationStatus } from '../../../../shared/models/applications/application-status.model';
import { BrandfetchResult, BrandfetchSearchService } from '../../services/brandfetch-search.service';


export type ApplicationFormMode = 'create' | 'edit';

export type ApplicationFormDialogData = {
  mode?: ApplicationFormMode;
  application?: {
    id: number;
    companyName: string;
    jobTitle: string;
    city: string;
    country: string;
    source: string;
    workMode: string;
    url: string;
    initialStatus?: ApplicationStatus;
    initialDate?: string;
    comment?: string;
  };
};

export type ApplicationFormValue = {
  companyName: string;
  companyDomain: string | null;
  companyLogoUrl: string | null;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  workMode: string;
  url: string;
  initialStatus: ApplicationStatus;
  initialDate: string | null;
  comment: string;
  createReminder: boolean;
};

@Component({
  selector: 'app-application-form-dialog',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './application-form-dialog.html',
})
export class ApplicationFormDialog{
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef =
    inject<MatDialogRef<ApplicationFormDialog, ApplicationFormValue | undefined>>(MatDialogRef);
  readonly data = inject<ApplicationFormDialogData | null>(MAT_DIALOG_DATA, {
    optional: true,
  });
  private readonly brandfetchSearch = inject(BrandfetchSearchService);

  readonly suggestions = signal<BrandfetchResult[]>([]);
  readonly selectedCompany = signal<BrandfetchResult | null>(null);
  readonly searching = signal(false);
  readonly searchFailed = signal(false);

  readonly mode: ApplicationFormMode = this.data?.mode ?? 'create';

  readonly title = computed(() =>
    this.mode === 'edit' ? 'Modifier la candidature' : 'Ajouter une candidature',
  );

  readonly subtitle = computed(() =>
    this.mode === 'edit'
      ? 'Mettez à jour les informations de cette candidature'
      : 'Ajoutez une nouvelle opportunité à votre suivi de candidature',
  );

  readonly sources = [
    'Indeed',
    'LinkedIn',
    'Welcome to the Jungle',
    'Site entreprise',
    'Email',
    'Recommandation',
    'Autre',
  ];

  readonly workModes = ['Remote', 'Hybride', 'Présentiel'];

  readonly countries = ['France', 'Belgique', 'Suisse', 'Luxembourg', 'Canada'];

  readonly statuses: { value: ApplicationStatus; label: string }[] = [
    { value: 'WISHLIST', label: 'À postuler' },
    { value: 'APPLIED', label: 'CV envoyé' },
    { value: 'FOLLOWED_UP', label: 'Relancé' },
    { value: 'FIRST_CONTACT', label: 'Premier contact' },
    { value: 'HR_INTERVIEW', label: 'Entretien RH' },
    { value: 'MANAGER_INTERVIEW', label: 'Entretien Manager' },
    { value: 'TECHNICAL_TEST', label: 'Test technique' },
    { value: 'OFFER_RECEIVED', label: 'Offre reçue' },
    { value: 'REJECTED', label: 'Rejeté' },
  ];

  readonly form = this.fb.nonNullable.group({
    companyName: [
      this.data?.application?.companyName ?? '',
      [Validators.required, Validators.maxLength(255)],
    ],
    jobTitle: [
      this.data?.application?.jobTitle ?? '',
      [Validators.required, Validators.maxLength(255)],
    ],
    city: [this.data?.application?.city ?? '', [Validators.required, Validators.maxLength(255)]],
    country: [this.data?.application?.country ?? 'France', [Validators.required]],
    source: [this.data?.application?.source ?? '', [Validators.required]],
    workMode: [this.data?.application?.workMode ?? '', [Validators.required]],
    url: [this.data?.application?.url ?? '', [Validators.maxLength(2048)]],
    initialStatus: [this.data?.application?.initialStatus ?? 'WISHLIST', [Validators.required]],
    initialDate: [this.data?.application?.initialDate ?? null],
    comment: [this.data?.application?.comment ?? '', [Validators.maxLength(1000)]],
    createReminder: [false],
  });

  constructor() {
    this.form.controls.companyName.valueChanges.pipe(
      tap(() => {
        this.selectedCompany.set(null);
        this.suggestions.set([]);
        this.searchFailed.set(false);
        this.searching.set(false);
      }),
      switchMap(value => {
        const query = value.trim();
        if (query.length < 2 || !this.brandfetchSearch.enabled) {
          return of([]);
        }

        return timer(250).pipe(
          switchMap(() => {
            if (this.selectedCompany()) {
              return of([]);
            }
            this.searching.set(true);
            return this.brandfetchSearch.search(query).pipe(
              catchError(() => {
                this.searchFailed.set(true);
                return of([]);
              }),
              tap(() => this.searching.set(false)),
            );
          }),
        );
      }),
      takeUntilDestroyed(),
    ).subscribe(results => this.suggestions.set(results));
  }

  selectCompany(company: BrandfetchResult): void {
    this.form.controls.companyName.setValue(company.name || company.domain);
    this.selectedCompany.set(company);
    this.suggestions.set([]);
  }

  close(): void {
    this.dialogRef.close(undefined);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();

      return
    }
    const value = this.form.getRawValue();
    this.dialogRef.close({
      ...value,
      companyDomain: this.selectedCompany()?.domain ?? null,
      initialDate: value.initialDate || null,
      companyLogoUrl: this.selectedCompany()?.icon ?? null,
    });
  }
}
