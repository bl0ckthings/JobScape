import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth';
import { IUserProfile } from '../../shared/models/userProfile';
import { Observable, tap } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiBaseUrl = environment.apiBaseUrl;
  readonly currentUser = signal<IUserProfile | null>(null);

  // LOGIN
  login(userLogin: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiBaseUrl + '/api/auth/login', userLogin).pipe(
      tap((res) => {
        if (!res?.token) throw new Error('Token not found');
        sessionStorage.setItem('token', res.token);
      }),
    );
  }

  loadCurrentUser(): void {
    this.http.get<IUserProfile>(`${this.apiBaseUrl}/api/users/me`).subscribe({
      next: (user) => this.currentUser.set(user),
    });
  }

  // REGISTER
  register(userRegister: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiBaseUrl + '/api/auth/register', userRegister);
  }

  // LOGOUT
  logout(): void {
    sessionStorage.removeItem('token');
    localStorage.removeItem('token');
    this.currentUser.set(null);
    void this.router.navigateByUrl('/auth/login');
  }

  get token(): string | null {
    return sessionStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }
}
