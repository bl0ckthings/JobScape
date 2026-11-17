export type DashboardPeriod = 'WEEK' | 'MONTH' | 'YEAR';

export interface DashboardActivityDto {
  timeline: number;
  nombreCandidatures: number;
  nombreReponses: number;
  nombreEntretien: number;
  nombreOffre: number;
}

export interface DashboardStat {
  value: number;
  change: number | null;
}

export interface DashboardResponse {
  firstName: string;
  applicationsSent: DashboardStat;
  responsesReceived: DashboardStat;
  interviews: DashboardStat;
  offers: DashboardStat;
  activity: DashboardActivityDto[];
}
