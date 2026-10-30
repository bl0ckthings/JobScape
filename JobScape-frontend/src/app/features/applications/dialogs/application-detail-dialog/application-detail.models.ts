import { ApplicationStatus } from '../../../../shared/models/applications/application-status.model';

export type DetailTab = 'steps' | 'contacts' | 'reminders';
export type DetailTone = 'blue' | 'green' | 'purple' | 'amber' | 'neutral';
export type StepState = 'interview' | 'todo' | 'done';
export interface DetailStep {
  id: number;
  nature: string;
  status: StepState;
  date: string;
  comment: string;
  contactId: number | null;
}
export interface DetailContact {
  id: number;
  name: string;
  role: string;
  company: string;
  email: string;
  phone: string;
  primary?: boolean;
}
export interface DetailReminder {
  id: number;
  title: string;
  date: string;
  time: string;
  comment: string;
  completed: boolean;
}
// Modèle de présentation de la maquette ; aucune dépendance aux appels HTTP.
export interface ApplicationDetailData {
  companyName: string;
  companyLogoUrl: string | null;
  jobTitle: string;
  source: string;
  workMode: string;
  status: ApplicationStatus;
  createdLabel: string;
  steps: DetailStep[];
  contacts: DetailContact[];
  reminders: DetailReminder[];
}
