import { Contact, ContactRequest } from '../../../../shared/models/applications/contact.model';
import { Reminder, ReminderRequest } from '../../../../shared/models/applications/reminder.model';
import { DetailContact, DetailReminder } from './application-detail.models';

export function buildContactRequest(
  contact: Omit<DetailContact, 'id'>,
  companyId: number,
): ContactRequest {
  return {
    name: contact.name.trim(),
    role: contact.role.trim() || null,
    email: contact.email.trim() || null,
    phone: contact.phone.trim() || null,
    companyId,
  };
}

export function toDetailContact(
  contact: Contact,
  companyName: string,
  primary?: boolean,
): DetailContact {
  return {
    id: contact.id,
    name: contact.name,
    role: contact.role ?? '',
    company: companyName,
    email: contact.email ?? '',
    phone: contact.phone ?? '',
    ...(primary === undefined ? {} : { primary }),
  };
}

export function buildReminderRequest(
  reminder: Omit<DetailReminder, 'id'>,
  applicationId: number,
): ReminderRequest {
  return {
    applicationId,
    reminderDate: `${reminder.date}T${reminder.time}:00`,
    comment: reminder.comment.trim() || null,
    completed: reminder.completed ?? false,
  };
}

export function toDetailReminder(reminder: Reminder): DetailReminder {
  const [date, time = ''] = reminder.reminderDate.split('T');

  return {
    id: reminder.id,
    title: 'Rappel',
    date,
    time: time.slice(0, 5),
    comment: reminder.comment ?? '',
    completed: reminder.completed,
  };
}
