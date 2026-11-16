import { ChartData } from 'chart.js';
import { DashboardActivityPoint } from './dashboard.models';

export function buildDashboardChartData(
  activity: DashboardActivityPoint[],
): ChartData<'line', number[], string> {
  return {
    labels: activity.map((point) => point.label),
    datasets: [
      {
        label: 'Candidatures',
        data: activity.map((point) => point.applications),
        borderColor: '#2563EB',
        backgroundColor: 'rgba(37, 99, 235, 0.12)',
        pointBackgroundColor: '#2563EB',
        fill: true,
        tension: 0.35,
      },
      {
        label: 'Retours',
        data: activity.map((point) => point.responses),
        borderColor: '#14B8A6',
        backgroundColor: 'rgba(20, 184, 166, 0.08)',
        pointBackgroundColor: '#14B8A6',
        tension: 0.35,
      },
      {
        label: 'Entretiens',
        data: activity.map((point) => point.interviews),
        borderColor: '#A855F7',
        backgroundColor: 'rgba(168, 85, 247, 0.08)',
        pointBackgroundColor: '#A855F7',
        tension: 0.35,
      },
    ],
  };
}
