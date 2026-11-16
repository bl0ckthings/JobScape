import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DashboardPeriod, DashboardResponse } from './dashboard.models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getDashboard(period: DashboardPeriod): Observable<DashboardResponse> {
    const params = new HttpParams().set('period', period);
    return this.http.get<DashboardResponse>(`${this.baseUrl}/api/dashboard`, { params });
  }
}
