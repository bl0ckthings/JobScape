import { Component, input, output } from '@angular/core';
import { DetailTab } from '../application-detail.models';

@Component({
  selector: 'app-application-detail-tabs',
  host: { class: 'block' },
  template: `
    <div
      class="tabs flex border-y border-detail-border bg-detail-surface"
      role="tablist"
      aria-label="Détails de la candidature"
    >
      @for (tab of tabs; track tab.id) {
        <button
          type="button"
          role="tab"
          class="group flex-1 min-w-0 flex items-center justify-center gap-[9px] py-[11px] px-2 border-b-2 border-transparent text-detail-muted text-[13px] font-semibold aria-selected:text-detail-blue aria-selected:border-detail-blue aria-selected:bg-[linear-gradient(180deg,transparent,#eff6ff80)] detail-mobile:pt-[17px] detail-mobile:px-1 detail-mobile:pb-[15px] detail-mobile:text-[11px] detail-mobile:gap-[5px]"
          [id]="'detail-tab-' + tab.id"
          [attr.aria-controls]="'detail-panel-' + tab.id"
          [attr.aria-selected]="active() === tab.id"
          [tabIndex]="active() === tab.id ? 0 : -1"
          (click)="selected.emit(tab.id)"
          (keydown)="onKeydown($event, tab.id)"
        >
          <span>{{ tab.label }}</span
          ><span
            class="count grid place-items-center min-w-[25px] h-6 px-[5px] rounded-full bg-[#f1f5f9] text-[11px] group-aria-selected:bg-[#eff6ff] detail-mobile:min-w-[19px] detail-mobile:h-[19px] detail-mobile:text-[10px]"
            >{{ counts()[tab.id] }}</span
          >
        </button>
      }
    </div>
  `,
})
export class ApplicationDetailTabs {
  readonly active = input<DetailTab>('steps');
  readonly counts = input<Record<DetailTab, number>>({ steps: 0, contacts: 0, reminders: 0 });
  readonly selected = output<DetailTab>();
  readonly tabs: { id: DetailTab; label: string }[] = [
    { id: 'steps', label: 'Étapes' },
    { id: 'contacts', label: 'Interlocuteurs' },
    { id: 'reminders', label: 'Rappels' },
  ];
  onKeydown(event: KeyboardEvent, active: DetailTab): void {
    const index = this.tabs.findIndex((tab) => tab.id === active);
    const next =
      event.key === 'ArrowRight'
        ? (index + 1) % 3
        : event.key === 'ArrowLeft'
          ? (index + 2) % 3
          : event.key === 'Home'
            ? 0
            : event.key === 'End'
              ? 2
              : null;
    if (next === null) return;
    event.preventDefault();
    this.selected.emit(this.tabs[next].id);
    const button = event.currentTarget as HTMLButtonElement;
    const tablist = button.parentElement!;
    (tablist.children[next] as HTMLButtonElement).focus();
  }
}
