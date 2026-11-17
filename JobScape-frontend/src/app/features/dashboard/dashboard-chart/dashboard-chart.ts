import { AfterViewInit, Component, ElementRef, OnChanges, OnDestroy, input, viewChild } from '@angular/core';
import {
  CategoryScale,
  Chart,
  Filler,
  Legend,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Tooltip,
} from 'chart.js';
import { buildDashboardChartData } from '../dashboard-chart';
import { DashboardActivityDto, DashboardPeriod } from '../dashboard.models';

Chart.register(CategoryScale, LinearScale, LineController, LineElement, PointElement, Filler, Tooltip, Legend);

@Component({
  selector: 'app-dashboard-chart',
  template: '<canvas #canvas aria-label="Évolution de la recherche d’emploi"></canvas>',
  host: { class: 'block h-[280px] sm:h-[340px]' },
})
export class DashboardChart implements AfterViewInit, OnChanges, OnDestroy {
  readonly activity = input.required<DashboardActivityDto[]>();
  readonly period = input.required<DashboardPeriod>();
  private readonly canvas = viewChild<ElementRef<HTMLCanvasElement>>('canvas');

  private chart?: Chart<'line', number[], string>;

  ngAfterViewInit(): void {
    this.renderChart();
  }

  ngOnChanges(): void {
    if (this.canvas()) this.renderChart();
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  private renderChart(): void {
    const canvas = this.canvas();
    if (!canvas) return;

    this.chart?.destroy();
    this.chart = new Chart<'line', number[], string>(canvas.nativeElement, {
      type: 'line',
      data: buildDashboardChartData(this.activity(), this.period()),
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { intersect: false, mode: 'index' },
        plugins: {
          legend: {
            position: 'bottom',
            align: 'start',
            labels: { usePointStyle: true, pointStyle: 'circle', boxWidth: 7, padding: 20 },
          },
          tooltip: {
            backgroundColor: '#0F172A',
            padding: 12,
            cornerRadius: 10,
            titleColor: '#FFFFFF',
            bodyColor: '#E2E8F0',
          },
        },
        scales: {
          x: { grid: { display: false }, border: { display: false } },
          y: {
            beginAtZero: true,
            ticks: { precision: 0 },
            grid: { color: '#EEF0F5' },
            border: { display: false },
          },
        },
      },
    });
  }
}
