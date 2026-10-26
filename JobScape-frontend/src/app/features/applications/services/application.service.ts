import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUserProfile } from '../../../shared/models/userProfile';
import { CreateApplication } from '../../../shared/models/requests/create-application';
import {
  ApplicationDto,
  ApplicationsByStatusResponse
} from '../../../shared/models/applications/applications-by-status-response.model';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getUser(id: number): Observable<IUserProfile> {
    return this.http.get<IUserProfile>(`${this.baseUrl}/users/${id}`);
  }

  createNewApplication(application: CreateApplication): Observable<ApplicationDto> {
    return this.http.post<ApplicationDto>(`${this.baseUrl}/applications/create`, application);
  }

  getApplicationByUserId(): Observable<ApplicationsByStatusResponse> {
    return this.http.get<ApplicationsByStatusResponse>(`${this.baseUrl}/applications/user`);
  }


}
