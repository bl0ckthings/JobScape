import { ApplicationStatus } from './application-status.model';

export type ApplicationCategory = 'WISHLIST' | 'APPLIED' | 'INTERVIEW' | 'OFFER' | 'REJECTED';

export interface ApplicationStepDto {
  id: number;
  comment: string | null;
  stepDate: string | null;
  stepStatus: ApplicationStatus;
  applicationId: number;
  contactId: number | null;
  createdAt: string;
  updatedAt: string;
}

export interface ApplicationDto {
  id: number;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  url: string | null;
  status: ApplicationStatus;
  workMode: string;
  createdAt: string;
  updatedAt: string;
  userId: number;
  companyId: number;
  companyName: string;
  companyDomain: string | null;
  companyLogoUrl: string | null;
  steps: ApplicationStepDto[];
}

export interface ApplicationsByStatusResponse {
  applicationsByStatus: Record<ApplicationCategory, ApplicationDto[]>;
}
