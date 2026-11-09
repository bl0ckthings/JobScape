import { Contact } from './contact.model';
import { Reminder } from './reminder.model';
import { ApplicationDto } from './applications-by-status-response.model';
import { Company } from './company.model';

export interface ApplicationDetails {
  application: ApplicationDto;
  company: Company | null;
  contacts: Contact[];
  reminders: Reminder[];
}
