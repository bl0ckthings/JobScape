import '@angular/compiler';
import assert from 'node:assert/strict';
import { test } from 'node:test';
import { HttpClient } from '@angular/common/http';
import { createEnvironmentInjector, runInInjectionContext } from '@angular/core';
import { of } from 'rxjs';
import { DashboardService } from './dashboard.service';

test('dashboard requests only parameters for the selected period', () => {
  const requests: string[] = [];
  const http = {
    get: (url: string, options: { params: { toString(): string } }) => {
      requests.push(`${url}?${options.params.toString()}`);
      return of(null);
    },
  };
  const injector = createEnvironmentInjector([{ provide: HttpClient, useValue: http }], null!);
  try {
    const service = runInInjectionContext(injector, () => new DashboardService());
    service.getDashboard(2026).subscribe();
    service.getDashboard(2026, 11).subscribe();
    service.getDashboard(2026, 11, 3).subscribe();
  } finally {
    injector.destroy();
  }
  assert.deepEqual(requests, [
    'http://localhost:8080/api/dashboard/stats?year=2026',
    'http://localhost:8080/api/dashboard/stats?year=2026&month=11',
    'http://localhost:8080/api/dashboard/stats?year=2026&month=11&week=3',
  ]);
});
