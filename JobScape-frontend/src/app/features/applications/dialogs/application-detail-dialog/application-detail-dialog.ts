import { Component, computed, DestroyRef, inject, LOCALE_ID, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { ApplicationService } from '../../services/application.service';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  ApplicationDetailData,
  DetailContact,
  DetailReminder,
  DetailStep,
  DetailTab,
} from './application-detail.models';
import { ApplicationDetailHeader } from './components/application-detail-header';
import { ApplicationDetailTabs } from './components/application-detail-tabs';
import { ApplicationStepsTab } from './tabs/application-steps-tab/application-steps-tab';
import { ApplicationContactsTab } from './tabs/application-contacts-tab/application-contacts-tab';
import { ApplicationRemindersTab } from './tabs/application-reminders-tab/application-reminders-tab';
import { ApplicationStepForm } from './forms/application-step-form/application-step-form';
import { ApplicationContactForm } from './forms/application-contact-form/application-contact-form';
import { ApplicationReminderForm } from './forms/application-reminder-form/application-reminder-form';
import { ContactService } from '../../services/contact.service';
import { ReminderService } from '../../services/reminder.service';
import {
  buildContactRequest,
  buildReminderRequest,
  toDetailContact,
  toDetailReminder,
} from './application-detail-api.mapper';

registerLocaleData(localeFr);

@Component({
  selector: 'app-application-detail-dialog',
  host: { class: 'block text-detail-ink' },
  imports: [
    ApplicationDetailHeader,
    ApplicationDetailTabs,
    ApplicationStepsTab,
    ApplicationContactsTab,
    ApplicationRemindersTab,
    ApplicationStepForm,
    ApplicationContactForm,
    ApplicationReminderForm,
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'fr' }],
  templateUrl: './application-detail-dialog.html',
})
export class ApplicationDetailDialog {
  private readonly applicationService = inject(ApplicationService);
  private readonly contactService = inject(ContactService);
  private readonly reminderService = inject(ReminderService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly dialogRef = inject(MatDialogRef<ApplicationDetailDialog, boolean>, {
    optional: true,
  });
  readonly data = signal(structuredClone(inject<ApplicationDetailData>(MAT_DIALOG_DATA)));
  readonly changed = signal(false);
  readonly tab = signal<DetailTab>('steps');
  readonly formOpen = signal(false);
  readonly savingStep = signal(false);
  readonly savingContact = signal(false);
  readonly savingReminder = signal(false);
  readonly stepSaveError = signal<string | null>(null);
  readonly contactSaveError = signal<string | null>(null);
  readonly reminderSaveError = signal<string | null>(null);
  readonly editedStep = signal<DetailStep | null>(null);
  readonly editedContact = signal<DetailContact | null>(null);
  readonly editedReminder = signal<DetailReminder | null>(null);
  readonly counts = computed(() => ({
    steps: this.data().steps.length,
    contacts: this.data().contacts.length,
    reminders: this.data().reminders.length,
  }));

  changeTab(tab: DetailTab): void {
    this.tab.set(tab);
    this.closeForm();
  }
  closeForm(): void {
    this.stepSaveError.set(null);
    this.contactSaveError.set(null);
    this.reminderSaveError.set(null);
    this.formOpen.set(false);
    this.editedStep.set(null);
    this.editedContact.set(null);
    this.editedReminder.set(null);
  }
  openStep(step: DetailStep | null = null): void {
    this.stepSaveError.set(null);
    this.editedStep.set(step);
    this.formOpen.set(true);
  }
  openContact(contact: DetailContact | null = null): void {
    this.contactSaveError.set(null);
    this.editedContact.set(contact);
    this.formOpen.set(true);
  }
  openReminder(reminder: DetailReminder | null = null): void {
    this.reminderSaveError.set(null);
    this.editedReminder.set(reminder);
    this.formOpen.set(true);
  }
  saveStep(value: Omit<DetailStep, 'id'>): void {
    if (this.savingStep()) return;

    const applicationId = this.data().applicationId;
    if (applicationId !== undefined) {
      const editedStep = this.editedStep();
      const request = {
        stepStatus: value.status,
        stepDate: value.date.includes('T') ? value.date : `${value.date}T00:00:00`,
        comment: value.comment,
        contactId: value.contactId,
      };
      const saveStep$ = editedStep
        ? this.applicationService.updateApplicationStep(editedStep.id, request)
        : this.applicationService.createApplicationStep(applicationId, request);

      this.savingStep.set(true);
      this.stepSaveError.set(null);
      saveStep$
        .pipe(
          takeUntilDestroyed(this.destroyRef),
          finalize(() => this.savingStep.set(false)),
        )
        .subscribe({
          next: (step) => {
            const savedStep: DetailStep = {
              id: step.id,
              status: step.stepStatus,
              date: step.stepDate ?? '',
              comment: step.comment ?? '',
              contactId: step.contactId,
            };
            this.data.update((data) => ({
              ...data,
              steps: (editedStep
                ? data.steps.map((item) => (item.id === savedStep.id ? savedStep : item))
                : [...data.steps, savedStep]
              ).sort((a, b) => b.date.localeCompare(a.date) || b.id - a.id),
            }));
            this.changed.set(true);
            this.closeForm();
          },
          error: () => this.stepSaveError.set('Impossible d’enregistrer l’étape. Réessayez.'),
        });
      return;
    }

    const id = this.editedStep()?.id;
    this.data.update((data) => ({
      ...data,
      steps:
        id === undefined
          ? [...data.steps, { ...value, id: this.nextId(data.steps) }]
          : data.steps.map((item) => (item.id === id ? { ...value, id } : item)),
    }));
    this.closeForm();
  }
  saveContact(value: Omit<DetailContact, 'id'>): void {
    if (this.savingContact()) return;

    const editedContact = this.editedContact();
    const request = buildContactRequest(value, this.data().companyId);

    const saveContact$ = editedContact
      ? this.contactService.updateContact(editedContact.id, request)
      : this.contactService.createContact(request);

    this.savingContact.set(true);
    this.contactSaveError.set(null);
    saveContact$
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.savingContact.set(false)),
      )
      .subscribe({
        next: (contact) => {
          const savedContact = toDetailContact(
            contact,
            this.data().companyName,
            editedContact?.primary,
          );

          this.data.update((data) => ({
            ...data,
            contacts: editedContact
              ? data.contacts.map((item) => (item.id === savedContact.id ? savedContact : item))
              : [...data.contacts, savedContact],
          }));

          this.changed.set(true);
          this.closeForm();
        },
        error: () => this.contactSaveError.set('Impossible d’enregistrer le contact.'),
      });
  }
  saveReminder(value: Omit<DetailReminder, 'id'>): void {
    if (this.savingReminder()) return;

    const applicationId = this.data().applicationId;
    if (applicationId === undefined) return;

    const editedReminder = this.editedReminder();
    const request = buildReminderRequest(value, applicationId);
    const saveReminder$ = editedReminder
      ? this.reminderService.updateReminder(editedReminder.id, request)
      : this.reminderService.createReminder(request);

    this.savingReminder.set(true);
    this.reminderSaveError.set(null);
    saveReminder$
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.savingReminder.set(false)),
      )
      .subscribe({
        next: (reminder) => {
          const savedReminder = toDetailReminder(reminder);
          this.data.update((data) => ({
            ...data,
            reminders: (editedReminder
              ? data.reminders.map((item) =>
                  item.id === savedReminder.id ? savedReminder : item,
                )
              : [...data.reminders, savedReminder]
            ).sort((a, b) => `${a.date}T${a.time}`.localeCompare(`${b.date}T${b.time}`)),
          }));
          this.changed.set(true);
          this.closeForm();
        },
        error: () => this.reminderSaveError.set('Impossible d’enregistrer le rappel.'),
      });
  }
  removeStep(): void {
    const id = this.editedStep()?.id;
    if (id === undefined) return;

    if (this.data().applicationId === undefined) {
      this.data.update((data) => ({ ...data, steps: data.steps.filter((item) => item.id !== id) }));
      this.closeForm();
      return;
    }

    this.savingStep.set(true);
    this.stepSaveError.set(null);
    this.applicationService
      .deleteApplicationStep(id)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.savingStep.set(false)),
      )
      .subscribe({
        next: () => {
          this.data.update((data) => ({
            ...data,
            steps: data.steps.filter((item) => item.id !== id),
          }));
          this.changed.set(true);
          this.closeForm();
        },
        error: () => this.stepSaveError.set('Impossible de supprimer l’étape. Réessayez.'),
      });
  }
  removeContact(): void {
    const id = this.editedContact()?.id;

    if (id === undefined) {
      return;
    }

    this.savingContact.set(true);
    this.contactSaveError.set(null);
    this.contactService
      .deleteContact(id)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.savingContact.set(false)),
      )
      .subscribe({
        next: () => {
          this.data.update((data) => ({
            ...data,
            contacts: data.contacts.filter((contact) => contact.id !== id),

            // Enlève localement le contact des étapes qui l’utilisaient.
            steps: data.steps.map((step) =>
              step.contactId === id ? { ...step, contactId: null } : step,
            ),
          }));

          this.changed.set(true);
          this.closeForm();
        },
        error: () => this.contactSaveError.set('Impossible de supprimer le contact.'),
      });
  }
  removeReminder(): void {
    const id = this.editedReminder()?.id;
    if (id === undefined) return;

    this.savingReminder.set(true);
    this.reminderSaveError.set(null);
    this.reminderService
      .deleteReminder(id)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.savingReminder.set(false)),
      )
      .subscribe({
        next: () => {
          this.data.update((data) => ({
            ...data,
            reminders: data.reminders.filter((item) => item.id !== id),
          }));
          this.changed.set(true);
          this.closeForm();
        },
        error: () => this.reminderSaveError.set('Impossible de supprimer le rappel.'),
      });
  }
  close(): void {
    this.dialogRef?.close(this.changed());
  }

  private nextId(items: { id: number }[]): number {
    return Math.max(0, ...items.map((item) => item.id)) + 1;
  }
}
