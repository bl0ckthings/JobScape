import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { DetailContact, DetailStep } from '../../application-detail.models';
import { Badge } from '../../../../../../shared/ui/badge/badge';
import { APPLICATION_STATUS_BADGE_CONFIG } from '../../../../../../shared/ui/badge/badge.config';
@Component({
  selector: 'app-application-steps-tab',
  host: { class: 'block' },
  imports: [DatePipe, Badge],
  templateUrl: './application-steps-tab.html',
})
export class ApplicationStepsTab {
  readonly statusConfig = APPLICATION_STATUS_BADGE_CONFIG;
  readonly steps = input<DetailStep[]>([]);
  readonly contacts = input<DetailContact[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailStep>();
  readonly editable = input(true);
  readonly canAdd = input(false);
  contactName(id: number | null): string {
    return this.contacts().find((item) => item.id === id)?.name ?? '';
  }
}
