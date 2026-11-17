import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DashboardResponse } from './dashboard.models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getDashboard(
    year: number,
    month?: number,
    week?: number,
  ): Observable<DashboardResponse> {
    let params = new HttpParams().set('year', year);

    if (month != null) {
      params = params.set('month', month);
    }

    if (week != null) {
      params = params.set('week', week);
    }

    return this.http.get<DashboardResponse>(`${this.baseUrl}/api/dashboard/stats`, { params });
  }
}
