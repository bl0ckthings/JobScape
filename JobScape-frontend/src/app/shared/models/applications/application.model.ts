import { ApplicationStatus } from './application-status.model';

export interface Application {
  id:number;
  companyName:string;
  jobTitle: string;
  city: string;
  country: string;
  source: string;
  url: string;
  status: ApplicationStatus;
  workMode:string;
}

export interface ApplicationStep {
  id:number;
  comment:string;
  stepDate: string;
  stepStatus:ApplicationStatus;
}
