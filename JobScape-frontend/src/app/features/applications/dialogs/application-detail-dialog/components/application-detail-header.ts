import { Component, computed, input, output, signal } from '@angular/core';
import { ApplicationDetailData } from '../application-detail.models';
import { DetailTag } from './detail-tag';
import { APPLICATION_STATUS_BADGE_CONFIG } from '../../../../../shared/ui/badge/badge.config';

@Component({
  selector: 'app-application-detail-header',
  imports: [DetailTag],
  template: `
    <header
      class="header p-6 bg-white detail-mobile:px-[18px] detail-mobile:pt-6 detail-mobile:pb-5"
    >
      <div
        class="heading-row flex items-start gap-3 detail-mobile:gap-[10px] detail-mobile:items-center"
      >
        <button
          class="back icon-button hidden detail-mobile:grid detail-mobile:min-w-5 detail-mobile:-ml-1 grid place-items-center h-7 min-w-7 text-detail-muted rounded-md text-[26px] leading-none hover:bg-detail-surface hover:text-detail-blue"
          type="button"
          aria-label="Fermer le détail"
          (click)="closed.emit()"
        >
          ‹
        </button>
        <div
          class="company-logo grid place-items-center flex-[0_0_42px] h-[42px] rounded-xl bg-[linear-gradient(135deg,#2563eb,#438aff)] text-white font-bold text-[14px] shadow-[0_6px_14px_#3478f61a] detail-mobile:basis-10 detail-mobile:h-10 detail-mobile:rounded-[10px]"
          aria-hidden="true"
        >
          @if (data().companyLogoUrl && !logoFailed()) {
            <img
              class="w-full h-full object-contain rounded-[inherit]"
              [src]="data().companyLogoUrl"
              alt=""
              (error)="logoFailed.set(true)"
            />
          } @else {
            {{ initials() }}
          }
        </div>
        <div class="heading flex-1 min-w-0">
          <h2
            class="text-[20px] leading-[1.25] font-bold m-0 mb-[2px] [overflow-wrap:anywhere] detail-mobile:text-[17px]"
            id="application-detail-title"
          >
            {{ data().companyName }}
          </h2>
          <p
            class="leading-4 text-[13px] text-detail-muted font-semibold detail-mobile:text-[12px] detail-mobile:font-normal"
          >
            {{ data().jobTitle }}
          </p>
        </div>
        <button
          type="button"
          class="icon-button favorite grid place-items-center h-7 min-w-7 text-detail-muted rounded-md text-[26px] leading-none hover:bg-detail-surface hover:text-detail-blue aria-pressed:text-[#b45f06]"
          aria-label="Marquer comme favori"
          [attr.aria-pressed]="favorite()"
          (click)="favorite.update(toggle)"
        >
          @if (favorite()) {
            <span aria-hidden="true">★</span>
          } @else {
            <img class="w-[21px] h-[21px]" src="assets/star.svg" alt="" />
          }
        </button>
        <button
          type="button"
          class="close icon-button detail-mobile:hidden grid place-items-center h-7 min-w-7 text-detail-muted rounded-md text-[26px] leading-none hover:bg-detail-surface hover:text-detail-blue"
          aria-label="Fermer le détail"
          (click)="closed.emit()"
        >
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="meta-row flex items-center flex-wrap gap-3 mt-4">
        <div class="tags flex flex-wrap gap-2 detail-mobile:gap-[6px]">
          <app-detail-tag>{{ data().source }}</app-detail-tag
          ><app-detail-tag tone="neutral">{{ data().workMode }}</app-detail-tag
          ><app-detail-tag [tone]="data().status === 'REJECTED' ? 'neutral' : 'green'">{{
            statusLabel()
          }}</app-detail-tag>
        </div>
        <p
          class="created flex items-center gap-[5px] ml-auto text-[11px] font-semibold text-detail-muted detail-mobile:w-full detail-mobile:mt-[6px] detail-mobile:mx-0 detail-mobile:mb-0 detail-mobile:font-normal"
        >
          <img class="w-[13px] h-[13px]" src="assets/calendar-days.svg" alt="" />{{
            data().createdLabel
          }}
        </p>
      </div>
    </header>
  `,
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
