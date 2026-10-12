import { Application, ApplicationStep } from './application.model';
import { Contact } from './contact.model';
import { Reminder } from './reminder.model';

export interface ApplicationDetails extends Application {
  contacts?:Contact[];
  reminders?:Reminder[];
  steps?:ApplicationStep[];
}
