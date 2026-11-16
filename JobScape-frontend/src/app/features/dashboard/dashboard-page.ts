import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { DashboardChart } from './dashboard-chart/dashboard-chart';
import { DashboardPeriod, DashboardResponse } from './dashboard.models';
import { DashboardService } from './dashboard.service';
import { NotificationBell } from '../notifications/notification-bell/notification-bell';

const EMPTY_DASHBOARD: DashboardResponse = {
  firstName: '',
  applicationsSent: { value: 0, change: null },
  responsesReceived: { value: 0, change: null },
  interviews: { value: 0, change: null },
  offers: { value: 0, change: null },
  activity: [],
};

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [DashboardChart, NotificationBell],
  templateUrl: './dashboard-page.html',
})
export class DashboardPage implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly destroyRef = inject(DestroyRef);

  readonly period = signal<DashboardPeriod>('MONTH');
  readonly dashboard = signal<DashboardResponse>(EMPTY_DASHBOARD);
  readonly loading = signal(true);
  readonly loadError = signal(false);
  readonly periods: { value: DashboardPeriod; label: string }[] = [
    { value: 'WEEK', label: 'Semaine' },
    { value: 'MONTH', label: 'Mois' },
    { value: 'YEAR', label: 'Année' },
  ];
  readonly statCards = computed(() => {
    const data = this.dashboard();
    return [
      { label: 'Candidatures envoyées', shortLabel: 'CA', ...data.applicationsSent },
      { label: 'Retours reçus', shortLabel: 'RE', ...data.responsesReceived },
      { label: 'Entretiens obtenus', shortLabel: 'EN', ...data.interviews },
      { label: 'Offres reçues', shortLabel: 'OF', ...data.offers },
    ];
  });

  ngOnInit(): void {
    this.loadDashboard();
  }

  selectPeriod(period: DashboardPeriod): void {
    if (period === this.period()) return;
    this.period.set(period);
    this.loadDashboard();
  }

  private loadDashboard(): void {
    this.loading.set(true);
    this.loadError.set(false);
    this.dashboardService
      .getDashboard(this.period())
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.loading.set(false)),
      )
      .subscribe({
        next: (dashboard) => this.dashboard.set(dashboard),
        error: () => this.loadError.set(true),
      });
  }
}
