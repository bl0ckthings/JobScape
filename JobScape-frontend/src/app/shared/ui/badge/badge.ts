import { Component, computed, input } from '@angular/core';
import { APPLICATION_STATUS_BADGE_CONFIG } from './badge.config';
import { ApplicationStatus } from '../../models/application-status.model';

@Component({
  selector: 'app-badge',
  imports: [],
  templateUrl: './badge.html',
  styleUrl: './badge.css',
})
export class Badge {
  status = input.required<ApplicationStatus>();

  badge = computed(() => APPLICATION_STATUS_BADGE_CONFIG[this.status()]);
}
