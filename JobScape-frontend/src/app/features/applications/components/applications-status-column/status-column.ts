import { Component, computed, input } from '@angular/core';
import { ApplicationCard } from '../application-card/application-card';
import { ApplicationCategory, ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';

@Component({
  selector: 'app-applications-status-column',
  imports: [ApplicationCard],
  templateUrl: './status-column.html',
  styleUrl: './status-column.css',
})
export class StatusColumn {
  readonly title = input('Wishlist');
  readonly category = input<ApplicationCategory>('WISHLIST');
  readonly applications = input<ApplicationDto[]>([]);
  readonly dotColor = computed(() => {
    switch (this.category()) {
      case 'APPLIED': return 'bg-[#6D5EFC]';
      case 'INTERVIEW': return 'bg-[#23C483]';
      default: return 'bg-gray-400';
    }
  });
}
