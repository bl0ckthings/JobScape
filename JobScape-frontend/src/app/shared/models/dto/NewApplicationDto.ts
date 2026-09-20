import { Application, ApplicationStep } from '../applications/application.model';

export interface NewApplicationDto {
  companyName: string;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  workMode: string;
  url?: string;
  initialStatus: string;
  initialDate: string | null;
  comment?: string;
  createReminder: boolean;
}
