import { Component, computed, input, output, signal } from '@angular/core';
import { ApplicationDetailData } from '../application-detail.models';
import { DetailTag } from './detail-tag';
import { APPLICATION_STATUS_BADGE_CONFIG } from '../../../../../shared/ui/badge/badge.config';

@Component({
  selector: 'app-application-detail-header',
  imports: [DetailTag],
  template: `
    <header class="header">
      <div class="heading-row">
        <button
          class="back icon-button"
          type="button"
          aria-label="Fermer le détail"
          (click)="closed.emit()"
        >
          ‹
        </button>
        <div class="company-logo" aria-hidden="true">
          @if (data().companyLogoUrl && !logoFailed()) {
            <img [src]="data().companyLogoUrl" alt="" (error)="logoFailed.set(true)" />
          } @else {
            {{ initials() }}
          }
        </div>
        <div class="heading">
          <h2 id="application-detail-title">{{ data().companyName }}</h2>
          <p>{{ data().jobTitle }}</p>
        </div>
        <button
          type="button"
          class="icon-button favorite"
          aria-label="Marquer comme favori"
          [attr.aria-pressed]="favorite()"
          (click)="favorite.update(toggle)"
        >
          @if (favorite()) {
            <span aria-hidden="true">★</span>
          } @else {
            <img src="assets/star.svg" alt="" />
          }
        </button>
        <button
          type="button"
          class="close icon-button"
          aria-label="Fermer le détail"
          (click)="closed.emit()"
        >
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="meta-row">
        <div class="tags">
          <app-detail-tag>{{ data().source }}</app-detail-tag
          ><app-detail-tag tone="neutral">{{ data().workMode }}</app-detail-tag
          ><app-detail-tag [tone]="data().status === 'REJECTED' ? 'neutral' : 'green'">{{
            statusLabel()
          }}</app-detail-tag>
        </div>
        <p class="created">
          <img src="assets/calendar-days.svg" alt="" />{{ data().createdLabel }}
        </p>
      </div>
    </header>
  `,
  styleUrl: './application-detail-header.css',
})
export class ApplicationDetailHeader {
  readonly data = input.required<ApplicationDetailData>();
  readonly closed = output<void>();
  readonly favorite = signal(false);
  readonly logoFailed = signal(false);
  readonly toggle = (value: boolean) => !value;
  readonly initials = computed(() => this.data().companyName.slice(0, 2).toUpperCase());
  readonly statusLabel = computed(() => APPLICATION_STATUS_BADGE_CONFIG[this.data().status].label);
}
