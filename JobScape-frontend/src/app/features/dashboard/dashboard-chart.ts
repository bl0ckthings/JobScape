import { ChartData } from 'chart.js';
import { DashboardActivityDto, DashboardPeriod } from './dashboard.models';

const MONTH_LABELS = [
  'Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin',
  'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc',
];

export function buildDashboardChartData(
  activity: DashboardActivityDto[],
  period: DashboardPeriod,
): ChartData<'line', number[], string> {
  return {
    labels: activity.map((point) => {
      switch (period) {
        case 'YEAR': return MONTH_LABELS[point.timeline - 1] ?? `${point.timeline}`;
        case 'MONTH': return `S${point.timeline}`;
        case 'WEEK': return `${point.timeline}`;
      }
    }),
    datasets: [
      {
        label: 'Candidatures',
        data: activity.map((point) => point.nombreCandidatures),
        borderColor: '#2563EB',
        backgroundColor: 'rgba(37, 99, 235, 0.12)',
        pointBackgroundColor: '#2563EB',
        fill: true,
        tension: 0.35,
      },
      {
        label: 'Retours',
        data: activity.map((point) => point.nombreReponses),
        borderColor: '#14B8A6',
        backgroundColor: 'rgba(20, 184, 166, 0.08)',
        pointBackgroundColor: '#14B8A6',
        tension: 0.35,
      },
      {
        label: 'Entretiens',
        data: activity.map((point) => point.nombreEntretien),
        borderColor: '#A855F7',
        backgroundColor: 'rgba(168, 85, 247, 0.08)',
        pointBackgroundColor: '#A855F7',
        tension: 0.35,
      },
    ],
  };
}
