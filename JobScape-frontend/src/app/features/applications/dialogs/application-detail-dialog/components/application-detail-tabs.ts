import { Component, input, output } from '@angular/core';
import { DetailTab } from '../application-detail.models';

@Component({
  selector: 'app-application-detail-tabs',
  template: `
    <div class="tabs" role="tablist" aria-label="Détails de la candidature">
      @for (tab of tabs; track tab.id) {
        <button
          type="button"
          role="tab"
          [id]="'detail-tab-' + tab.id"
          [attr.aria-controls]="'detail-panel-' + tab.id"
          [attr.aria-selected]="active() === tab.id"
          [tabIndex]="active() === tab.id ? 0 : -1"
          (click)="selected.emit(tab.id)"
          (keydown)="onKeydown($event, tab.id)"
        >
          <span>{{ tab.label }}</span
          ><span class="count">{{ counts()[tab.id] }}</span>
        </button>
      }
    </div>
  `,
  styles: [
    `
      :host {
        display: block;
      }
      .tabs {
        display: flex;
        border-block: 1px solid var(--detail-border);
        background: var(--detail-surface);
      }
      button {
        flex: 1;
        min-width: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 9px;
        padding: 11px 8px;
        border-bottom: 2px solid transparent;
        color: var(--detail-muted);
        font-size: 13px;
        font-weight: 600;
      }
      button[aria-selected='true'] {
        color: var(--detail-blue);
        border-bottom-color: var(--detail-blue);
        background: linear-gradient(180deg, transparent, #eff6ff80);
      }
      .count {
        display: grid;
        place-items: center;
        min-width: 25px;
        height: 24px;
        padding: 0 5px;
        border-radius: 50%;
        background: #f1f5f9;
        font-size: 11px;
      }
      button[aria-selected='true'] .count {
        background: #eff6ff;
      }
      @media (max-width: 600px) {
        button {
          padding: 17px 4px 15px;
          font-size: 11px;
          gap: 5px;
        }
        .count {
          min-width: 19px;
          height: 19px;
          font-size: 10px;
        }
      }
    `,
  ],
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
