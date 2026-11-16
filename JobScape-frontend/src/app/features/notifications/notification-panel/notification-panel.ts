import { DatePipe } from '@angular/common';
import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { JobNotification } from '../notification.models';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-notification-panel',
  imports: [DatePipe],
  templateUrl: './notification-panel.html',
})
export class NotificationPanel implements OnInit {
  private readonly notificationService = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);

  readonly notifications = signal<JobNotification[]>([]);
  readonly loading = signal(true);
  readonly error = signal(false);

  ngOnInit(): void {
    this.loadNotifications();
  }

  markAsRead(notification: JobNotification): void {
    if (notification.readAt) return;

    this.notificationService
      .markAsRead(notification.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (updated) => {
          this.notifications.update((notifications) =>
            notifications.map((item) => (item.id === updated.id ? updated : item)),
          );
        },
      });
  }

  remove(notification: JobNotification, event: Event): void {
    event.stopPropagation();
    this.notificationService
      .deleteNotification(notification.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.notifications.update((notifications) =>
            notifications.filter((item) => item.id !== notification.id),
          );
        },
      });
  }

  private loadNotifications(): void {
    this.loading.set(true);
    this.error.set(false);
    this.notificationService
      .getNotifications()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (notifications) => {
          this.notifications.set(notifications);
          this.loading.set(false);
        },
        error: () => {
          this.error.set(true);
          this.loading.set(false);
        },
      });
  }
}
