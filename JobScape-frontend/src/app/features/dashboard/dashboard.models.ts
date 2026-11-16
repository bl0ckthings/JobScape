export type DashboardPeriod = 'WEEK' | 'MONTH' | 'YEAR';

export interface DashboardStat {
  value: number;
  change: number | null;
}

export interface DashboardActivityPoint {
  label: string;
  applications: number;
  responses: number;
  interviews: number;
}

export interface DashboardResponse {
  firstName: string;
  applicationsSent: DashboardStat;
  responsesReceived: DashboardStat;
  interviews: DashboardStat;
  offers: DashboardStat;
  activity: DashboardActivityPoint[];
}
