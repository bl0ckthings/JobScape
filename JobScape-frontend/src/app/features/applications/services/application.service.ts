import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUserProfile } from '../../../shared/models/userProfile';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {

  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {
  }

  getUser(id:number):Observable<IUserProfile> {
    return this.http.get<IUserProfile>(`${this.baseUrl}/users/${id}`);
  }

}
