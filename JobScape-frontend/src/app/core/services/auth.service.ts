import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth';
import { Observable, catchError, finalize, of, tap, timeout } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiBaseUrl = environment.apiBaseUrl;

  // LOGIN
  login(userLogin: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiBaseUrl + '/api/auth/login', userLogin).pipe(
      tap((res) => {
        if (!res?.token) throw new Error('Token not found');
        sessionStorage.setItem('token', res.token);
      }),
    );
  }

  // REGISTER
  register(userRegister: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiBaseUrl + '/api/auth/register', userRegister);
  }

  // LOGOUT
  logout(): void {
    this.http
      .post<void>(`${this.apiBaseUrl}/api/auth/logout`, {})
      .pipe(
        timeout(5000),
        catchError(() => of(undefined)),
        finalize(() => {
          sessionStorage.removeItem('token');
          localStorage.removeItem('token');
          void this.router.navigateByUrl('/login');
        }),
      )
      .subscribe();
  }

  get token(): string | null {
    return sessionStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }
}
