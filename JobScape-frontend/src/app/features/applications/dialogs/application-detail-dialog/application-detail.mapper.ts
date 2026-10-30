import { ApplicationDetails } from '../../../../shared/models/applications/application-detail.model';
import { ApplicationDetailData } from './application-detail.models';

export function toApplicationDetailData(response: ApplicationDetails): ApplicationDetailData {
  const application = response.application;

  const createdDate = new Intl.DateTimeFormat('fr-FR', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  }).format(new Date(application.createdAt));

  return {
    companyName: application.companyName,
    companyLogoUrl: application.companyLogoUrl,
    jobTitle: application.jobTitle,
    source: application.source,
    workMode: application.workMode,
    status: application.status,
    createdLabel: `Créée le ${createdDate}`,
    readOnly: true,

    steps: application.steps.map((step) => ({
      id: step.id,
      status: step.stepStatus,
      date: step.stepDate ?? '',
      comment: step.comment ?? '',
      contactId: step.contactId,
    })),

    contacts: response.contacts.map((contact) => ({
      id: contact.id,
      name: contact.name,
      role: contact.role ?? '',
      company: application.companyName,
      email: contact.email ?? '',
      phone: contact.phone ?? '',
    })),

    reminders: response.reminders.map((reminder) => {
      const [date, time = ''] = reminder.reminderDate.split('T');

      return {
        id: reminder.id,
        title: 'Rappel',
        date,
        time: time.slice(0, 5),
        comment: reminder.comment ?? '',
        completed: null,
      };
    }),
  };
}
