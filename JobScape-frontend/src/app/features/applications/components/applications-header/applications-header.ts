import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-applications-header',
  imports: [],
  templateUrl: './applications-header.html',
  styleUrl: './applications-header.css',
})
export class ApplicationsHeader {
  readonly notificationsOpen = signal(false);
  readonly sortOpen = signal(false);
  readonly sortLabel = signal('Trier par');

  toggleNotifications(): void {
    this.sortOpen.set(false);
    this.notificationsOpen.update(open => !open);
  }

  toggleSort(): void {
    this.notificationsOpen.set(false);
    this.sortOpen.update(open => !open);
  }

  selectSort(label: string): void {
    this.sortLabel.set(label);
    this.sortOpen.set(false);
  }
}
