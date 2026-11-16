import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JobNotification } from './notification.models';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getNotifications(): Observable<JobNotification[]> {
    return this.http.get<JobNotification[]>(`${this.baseUrl}/api/notifications`);
  }

  markAsRead(id: string): Observable<JobNotification> {
    return this.http.put<JobNotification>(`${this.baseUrl}/api/notifications/${id}/read`, {});
  }

  deleteNotification(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/notifications/${id}`);
  }
}
