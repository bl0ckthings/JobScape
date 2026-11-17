import '@angular/compiler';
import assert from 'node:assert/strict';
import { test } from 'node:test';
import { createEnvironmentInjector, runInInjectionContext } from '@angular/core';
import { Subject } from 'rxjs';
import { DashboardPage } from './dashboard-page';
import { DashboardResponse } from './dashboard.models';
import { DashboardService } from './dashboard.service';

const dashboard: DashboardResponse = {
  firstName: 'Alex',
  applicationsSent: { value: 6, change: null },
  responsesReceived: { value: 4, change: null },
  interviews: { value: 2, change: null },
  offers: { value: 1, change: null },
  activity: [],
};

test('period changes request matching dates and ignore the previous response', () => {
  const requests: { args: number[]; response: Subject<DashboardResponse> }[] = [];
  const service = {
    getDashboard: (...args: number[]) => {
      const response = new Subject<DashboardResponse>();
      requests.push({ args, response });
      return response;
    },
  };
  const injector = createEnvironmentInjector([{ provide: DashboardService, useValue: service }], null!);
  try {
    const page = runInInjectionContext(injector, () => new DashboardPage());
    const now = new Date();
    page.ngOnInit();
    assert.deepEqual(requests[0].args, [now.getFullYear(), now.getMonth() + 1, undefined]);
    page.selectPeriod('WEEK');
    assert.deepEqual(requests[1].args, [now.getFullYear(), now.getMonth() + 1, Math.floor((now.getDate() - 1) / 7) + 1]);
    assert.equal(requests[0].response.observed, false);
    requests[0].response.next(dashboard);
    assert.equal(page.dashboard().firstName, '');
    assert.equal(page.loading(), true);
    requests[1].response.next(dashboard);
    requests[1].response.complete();
    assert.equal(page.dashboard().firstName, 'Alex');
    assert.equal(page.loading(), false);
    page.selectPeriod('YEAR');
    assert.deepEqual(requests[2].args, [now.getFullYear(), undefined, undefined]);
    assert.equal(page.dashboard().applicationsSent.value, 0);
    requests[2].response.error(new Error('network'));
    assert.equal(page.loadError(), true);
    assert.equal(page.dashboard().applicationsSent.value, 0);
    assert.equal(page.loading(), false);
  } finally {
    injector.destroy();
  }
});
