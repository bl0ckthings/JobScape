import { Component, computed, input, OnInit, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DetailContact, DetailStep, StepState } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
import { ApplicationFormSelect } from '../../../application-form-dialog/application-form-select';
@Component({
  selector: 'app-application-step-form',
  imports: [ReactiveFormsModule, DetailTag, ApplicationFormSelect],
  templateUrl: './application-step-form.html',
  styleUrl: '../../detail-form.css',
})
export class ApplicationStepForm implements OnInit {
  readonly step = input<DetailStep | null>(null);
  readonly contacts = input<DetailContact[]>([]);
  readonly saved = output<Omit<DetailStep, 'id'>>();
  readonly cancelled = output<void>();
  readonly removed = output<void>();
  readonly form = new FormGroup({
    nature: new FormControl('Premier contact', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    status: new FormControl('interview', { nonNullable: true, validators: [Validators.required] }),
    comment: new FormControl('', { nonNullable: true }),
    date: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    contactId: new FormControl('', { nonNullable: true }),
  });
  readonly natureOptions = [
    'Premier contact',
    'Entretien RH',
    'Entretien Manager',
    'Test technique',
    'Relance',
    'Offre reçue',
  ].map((value) => ({ value, label: value }));
  readonly statusOptions = [
    { value: 'interview', label: 'Entretien' },
    { value: 'todo', label: 'À faire' },
    { value: 'done', label: 'Effectué' },
  ];
  readonly contactOptions = computed(() => [
    { value: '', label: 'Aucun interlocuteur' },
    ...this.contacts().map((item) => ({ value: String(item.id), label: item.name })),
  ]);
  ngOnInit(): void {
    const step = this.step();
    if (step)
      this.form.patchValue({
        ...step,
        contactId: step.contactId === null ? '' : String(step.contactId),
      });
  }
  selectedContact(): DetailContact | undefined {
    return this.contacts().find((item) => String(item.id) === this.form.controls.contactId.value);
  }
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    this.saved.emit({
      ...value,
      status: value.status as StepState,
      contactId: value.contactId ? Number(value.contactId) : null,
    });
  }
}
