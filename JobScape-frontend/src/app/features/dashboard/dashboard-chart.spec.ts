import assert from 'node:assert/strict';
import { test } from 'node:test';
import { buildDashboardChartData } from './dashboard-chart';
import { DashboardActivityDto } from './dashboard.models';

const activity: DashboardActivityDto[] = [{
  timeline: 3,
  nombreCandidatures: 6,
  nombreReponses: 4,
  nombreEntretien: 2,
  nombreOffre: 1,
}];

test('chart uses API counts and labels for each period', () => {
  assert.deepEqual(buildDashboardChartData(activity, 'YEAR').labels, ['Mar']);
  assert.deepEqual(buildDashboardChartData(activity, 'MONTH').labels, ['S3']);
  assert.deepEqual(buildDashboardChartData(activity, 'WEEK').labels, ['3']);
  assert.deepEqual(
    buildDashboardChartData(activity, 'WEEK').datasets.map((dataset) => dataset.data),
    [[6], [4], [2]],
  );
});

test('week labels use day of month, including dates after the first week', () => {
  assert.deepEqual(buildDashboardChartData([{ ...activity[0], timeline: 15 }], 'WEEK').labels, ['15']);
});

test('empty activity keeps the three existing series', () => {
  const chart = buildDashboardChartData([], 'MONTH');
  assert.deepEqual(chart.labels, []);
  assert.deepEqual(chart.datasets.map((dataset) => dataset.data), [[], [], []]);
});
