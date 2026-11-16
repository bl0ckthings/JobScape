import { Component, signal } from '@angular/core';
import { NotificationPanel } from '../notification-panel/notification-panel';

@Component({
  selector: 'app-notification-bell',
  imports: [NotificationPanel],
  templateUrl: './notification-bell.html',
})
export class NotificationBell {
  readonly open = signal(false);

  toggle(): void {
    this.open.update((open) => !open);
  }
}
