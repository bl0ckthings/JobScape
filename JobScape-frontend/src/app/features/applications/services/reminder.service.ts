import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Reminder, ReminderRequest } from '../../../shared/models/applications/reminder.model';

@Injectable({ providedIn: 'root' })
export class ReminderService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  createReminder(reminder: ReminderRequest): Observable<Reminder> {
    return this.http.post<Reminder>(`${this.baseUrl}/api/reminders/create`, reminder);
  }

  updateReminder(id: number, reminder: ReminderRequest): Observable<Reminder> {
    return this.http.put<Reminder>(`${this.baseUrl}/api/reminders/update/${id}`, reminder);
  }

  deleteReminder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/reminders/delete/${id}`);
  }
}
