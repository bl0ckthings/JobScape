import { Component, computed, inject, LOCALE_ID, signal } from '@angular/core';
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

registerLocaleData(localeFr);

@Component({
  selector: 'app-application-detail-dialog',
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
  styleUrl: './application-detail-dialog.css',
})
export class ApplicationDetailDialog {
  private readonly dialogRef = inject(MatDialogRef<ApplicationDetailDialog>, { optional: true });
  // L'aperçu travaille sur une copie : aucune mutation des données fournies, aucun appel API.
  readonly data = signal(structuredClone(inject<ApplicationDetailData>(MAT_DIALOG_DATA)));
  readonly tab = signal<DetailTab>('steps');
  readonly formOpen = signal(false);
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
    this.formOpen.set(false);
    this.editedStep.set(null);
    this.editedContact.set(null);
    this.editedReminder.set(null);
  }
  openStep(step: DetailStep | null = null): void {
    this.editedStep.set(step);
    this.formOpen.set(true);
  }
  openContact(contact: DetailContact | null = null): void {
    this.editedContact.set(contact);
    this.formOpen.set(true);
  }
  openReminder(reminder: DetailReminder | null = null): void {
    this.editedReminder.set(reminder);
    this.formOpen.set(true);
  }
  saveStep(value: Omit<DetailStep, 'id'>): void {
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
    const id = this.editedContact()?.id;
    this.data.update((data) => ({
      ...data,
      contacts:
        id === undefined
          ? [...data.contacts, { ...value, id: this.nextId(data.contacts) }]
          : data.contacts.map((item) => (item.id === id ? { ...item, ...value, id } : item)),
    }));
    this.closeForm();
  }
  saveReminder(value: Omit<DetailReminder, 'id'>): void {
    const id = this.editedReminder()?.id;
    this.data.update((data) => ({
      ...data,
      reminders:
        id === undefined
          ? [...data.reminders, { ...value, id: this.nextId(data.reminders) }]
          : data.reminders.map((item) => (item.id === id ? { ...value, id } : item)),
    }));
    this.closeForm();
  }
  removeStep(): void {
    const id = this.editedStep()?.id;
    this.data.update((data) => ({ ...data, steps: data.steps.filter((item) => item.id !== id) }));
    this.closeForm();
  }
  removeContact(): void {
    const id = this.editedContact()?.id;
    this.data.update((data) => ({
      ...data,
      contacts: data.contacts.filter((item) => item.id !== id),
      steps: data.steps.map((step) =>
        step.contactId === id ? { ...step, contactId: null } : step,
      ),
    }));
    this.closeForm();
  }
  removeReminder(): void {
    const id = this.editedReminder()?.id;
    this.data.update((data) => ({
      ...data,
      reminders: data.reminders.filter((item) => item.id !== id),
    }));
    this.closeForm();
  }
  close(): void {
    this.dialogRef?.close();
  }
  private nextId(items: { id: number }[]): number {
    return Math.max(0, ...items.map((item) => item.id)) + 1;
  }
}
