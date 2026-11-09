import { Component, computed, input, OnInit, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DetailContact, DetailStep } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
import { ApplicationFormSelect } from '../../../application-form-dialog/application-form-select';
import { ApplicationStatus } from '../../../../../../shared/models/applications/application-status.model';
import { APPLICATION_STATUS_BADGE_CONFIG } from '../../../../../../shared/ui/badge/badge.config';

@Component({
  selector: 'app-application-step-form',
  host: { class: 'flex flex-col flex-1 min-h-0' },
  imports: [ReactiveFormsModule, DetailTag, ApplicationFormSelect],
  templateUrl: './application-step-form.html',
})
export class ApplicationStepForm implements OnInit {
  readonly step = input<DetailStep | null>(null);
  readonly contacts = input<DetailContact[]>([]);
  readonly saving = input(false);
  readonly error = input<string | null>(null);
  readonly saved = output<Omit<DetailStep, 'id'>>();
  readonly cancelled = output<void>();
  readonly removed = output<void>();
  readonly form = new FormGroup({
    status: new FormControl<ApplicationStatus>('FIRST_CONTACT', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    comment: new FormControl('', { nonNullable: true }),
    date: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    contactId: new FormControl('', { nonNullable: true }),
  });

  // 1. Récuperer les status disponibles
  readonly statuses = Object.keys(APPLICATION_STATUS_BADGE_CONFIG) as ApplicationStatus[];

  readonly statusOptions = this.statuses.map((status) => {
    const config = APPLICATION_STATUS_BADGE_CONFIG[status];

    return {
      value: status,
      label: config.label,
    };
  });

  readonly contactOptions = computed(() => [
    { value: '', label: 'Aucun interlocuteur' },
    ...this.contacts().map((item) => ({ value: String(item.id), label: item.name })),
  ]);
  ngOnInit(): void {
    const step = this.step();
    if (!step) return;

    this.form.patchValue({
      status: step.status,
      comment: step.comment,
      date: step.date,
      contactId: step.contactId === null ? '' : String(step.contactId),
    });
  }
  selectedContact(): DetailContact | undefined {
    return this.contacts().find((item) => String(item.id) === this.form.controls.contactId.value);
  }
  submit(): void {
    if (this.saving()) return;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    this.saved.emit({
      status: value.status,
      date: value.date,
      comment: value.comment,
      contactId: value.contactId === '' ? null : Number(value.contactId),
    });
  }
}
