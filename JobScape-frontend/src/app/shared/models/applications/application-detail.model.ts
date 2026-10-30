import { Application, ApplicationStep } from './application.model';
import { Contact } from './contact.model';
import { Reminder } from './reminder.model';
import { ApplicationDto } from './applications-by-status-response.model';

export interface ApplicationDetails {
  application: ApplicationDto;
  contacts: Contact[];
  reminders: Reminder[];
}
