import { Component, signal } from '@angular/core';
import { NotificationBell } from '../../../notifications/notification-bell/notification-bell';

@Component({
  selector: 'app-applications-header',
  imports: [NotificationBell],
  templateUrl: './applications-header.html',
  styleUrl: './applications-header.css',
})
export class ApplicationsHeader {
  readonly sortOpen = signal(false);
  readonly sortLabel = signal('Trier par');

  toggleSort(): void {
    this.sortOpen.update(open => !open);
  }

  selectSort(label: string): void {
    this.sortLabel.set(label);
    this.sortOpen.set(false);
  }
}
