import { Component, input, OnInit, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DetailReminder } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-reminder-form',
  imports: [ReactiveFormsModule, DetailTag],
  templateUrl: './application-reminder-form.html',
  styleUrl: '../../detail-form.css',
})
export class ApplicationReminderForm implements OnInit {
  readonly reminder = input<DetailReminder | null>(null);
  readonly companyName = input('');
  readonly saved = output<Omit<DetailReminder, 'id'>>();
  readonly cancelled = output<void>();
  readonly removed = output<void>();
  readonly quickDay = signal<number | null>(null);
  readonly quickOptions = [
    { days: 1, label: 'Demain' },
    { days: 3, label: 'Dans 3 jours' },
    { days: 7, label: 'Dans 7 jours' },
  ];
  readonly form = new FormGroup({
    date: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    time: new FormControl('09:00', { nonNullable: true, validators: [Validators.required] }),
    comment: new FormControl('', { nonNullable: true }),
  });
  ngOnInit(): void {
    if (this.reminder()) this.form.patchValue(this.reminder()!);
  }
  chooseDate(days: number): void {
    const date = new Date();
    date.setDate(date.getDate() + days);
    // On conserve la date locale, sans décalage lié à une conversion UTC.
    this.form.controls.date.setValue(
      `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`,
    );
    this.quickDay.set(days);
  }
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saved.emit({
      ...this.form.getRawValue(),
      title: this.reminder()?.title ?? 'Relancer l’entreprise',
      completed: this.reminder()?.completed ?? false,
    });
  }
}
