import { ApplicationStatus } from '../applications/application-status.model';

export interface CreateApplicationStep {
  stepStatus: ApplicationStatus;
  stepDate: string;
  comment: string;
  contactId: number | null;
}
