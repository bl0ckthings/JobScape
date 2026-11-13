import { ApplicationDetails } from '../../../../shared/models/applications/application-detail.model';
import { ApplicationDetailData } from './application-detail.models';
import { getCompanyLogoUrl } from '../../company-logo';

export function toApplicationDetailData(response: ApplicationDetails): ApplicationDetailData {
  const application = response.application;
  const company = response.company;
  const companyName = company?.name?.trim() || 'Entreprise';

  const createdDate = new Intl.DateTimeFormat('fr-FR', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  }).format(new Date(application.createdAt));

  return {
    applicationId: application.id,
    companyName,
    companyLogoUrl: getCompanyLogoUrl(company?.logoUrl, company?.domain),
    jobTitle: application.jobTitle,
    source: application.source,
    workMode: application.workMode,
    status: application.status,
    createdLabel: `Créée le ${createdDate}`,
    readOnly: false,

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
      company: companyName,
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
