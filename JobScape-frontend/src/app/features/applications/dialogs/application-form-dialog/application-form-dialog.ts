import { Component, computed, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { NgIf } from '@angular/common';

import { BaseInput } from '../../../../shared/ui/base-input/input';
import { BaseSelect } from '../../../../shared/ui/base-select/base-select';
import { BaseTextarea } from '../../../../shared/ui/base-textarea/base-textarea';
import { DateInput } from '../../../../shared/ui/date-input/date-input';

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
    initialStatus?: string;
    initialDate?: string;
    comment?: string;
  };
};

export type ApplicationFormValue = {
  companyName: string;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  workMode: string;
  url: string;
  initialStatus: string;
  initialDate: string | null;
  comment: string;
  createReminder: boolean;
};

@Component({
  selector: 'app-application-form-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,


    BaseInput,
    BaseSelect,
    BaseTextarea,
    DateInput,
  ],
  templateUrl: './application-form-dialog.html',
})
export class ApplicationFormDialog{
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(DialogRef<ApplicationFormValue | undefined>);
  readonly data = inject<ApplicationFormDialogData | null>(DIALOG_DATA, {
    optional: true,
  });

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

  readonly statuses = [
    'À postuler',
    'CV envoyé',
    'Relancé',
    'Premier contact',
    'Entretien RH',
    'Entretien Manager',
    'Test technique',
    'Offre reçue',
    'Rejeté',
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
    source: [this.data?.application?.source ?? 'Indeed', [Validators.required]],
    workMode: [this.data?.application?.workMode ?? 'Remote', [Validators.required]],
    url: [this.data?.application?.url ?? '', [Validators.required, Validators.maxLength(2048)]],
    initialStatus: [this.data?.application?.initialStatus ?? 'CV envoyé', [Validators.required]],
    initialDate: [this.data?.application?.initialDate ?? null],
    comment: [this.data?.application?.comment ?? '', [Validators.maxLength(1000)]],
    createReminder: [false],
  });

  close(): void {
    this.dialogRef.close(undefined);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.dialogRef.close(this.form.getRawValue());
  }
}
