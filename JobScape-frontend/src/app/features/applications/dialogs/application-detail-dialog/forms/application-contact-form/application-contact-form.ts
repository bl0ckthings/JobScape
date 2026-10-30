import { Component, input, OnInit, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DetailContact } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-contact-form',
  imports: [ReactiveFormsModule, DetailTag],
  templateUrl: './application-contact-form.html',
  styleUrl: '../../detail-form.css',
})
export class ApplicationContactForm implements OnInit {
  readonly contact = input<DetailContact | null>(null);
  readonly companyName = input('');
  readonly jobTitle = input('');
  readonly saved = output<Omit<DetailContact, 'id'>>();
  readonly cancelled = output<void>();
  readonly removed = output<void>();
  readonly form = new FormGroup({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/\S/)],
    }),
    role: new FormControl('', { nonNullable: true }),
    company: new FormControl('', { nonNullable: true }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.email] }),
    phone: new FormControl('', { nonNullable: true }),
  });
  ngOnInit(): void {
    this.form.patchValue(this.contact() ?? { company: this.companyName() });
  }
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    this.saved.emit({ ...value, name: value.name.trim(), email: value.email.trim() });
  }
}
