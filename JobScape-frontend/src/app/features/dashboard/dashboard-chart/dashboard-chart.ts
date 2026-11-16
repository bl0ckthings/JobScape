import { AfterViewInit, Component, ElementRef, Input, OnChanges, OnDestroy, ViewChild } from '@angular/core';
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
import { DashboardActivityPoint } from '../dashboard.models';

Chart.register(CategoryScale, LinearScale, LineController, LineElement, PointElement, Filler, Tooltip, Legend);

@Component({
  selector: 'app-dashboard-chart',
  template: '<canvas #canvas aria-label="Évolution de la recherche d’emploi"></canvas>',
  host: { class: 'block h-[280px] sm:h-[340px]' },
})
export class DashboardChart implements AfterViewInit, OnChanges, OnDestroy {
  @Input({ required: true }) activity: DashboardActivityPoint[] = [];
  @ViewChild('canvas') private canvas?: ElementRef<HTMLCanvasElement>;

  private chart?: Chart<'line', number[], string>;

  ngAfterViewInit(): void {
    this.renderChart();
  }

  ngOnChanges(): void {
    if (this.canvas) this.renderChart();
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  private renderChart(): void {
    if (!this.canvas) return;

    this.chart?.destroy();
    this.chart = new Chart<'line', number[], string>(this.canvas.nativeElement, {
      type: 'line',
      data: buildDashboardChartData(this.activity),
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
