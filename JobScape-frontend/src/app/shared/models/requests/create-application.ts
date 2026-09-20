import { ApplicationStatus } from '../applications/application-status.model';

export interface CreateApplication {
  companyName: string;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  workMode: string;
  url?: string;
  initialStatus: ApplicationStatus;
  initialDate: string | null;
  comment?: string;
  createReminder: boolean;
}
