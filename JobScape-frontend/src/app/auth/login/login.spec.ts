import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Login } from './login';
import { AuthService } from '../../core/services/auth.service';
import { provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let navigateByUrl: ReturnType<typeof vi.spyOn>;
  const login = vi.fn(() => of({}));

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        { provide: AuthService, useValue: { login } },
        provideRouter([]),
      ],
    }).compileComponents();

    navigateByUrl = vi.spyOn(TestBed.inject(Router), 'navigateByUrl');
    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('opens Candidatures after a successful login', () => {
    component.form.setValue({ email: 'test@example.com', password: 'password' });

    component.submit();

    expect(navigateByUrl).toHaveBeenCalledWith('/applications');
  });

  it('submits the login form from the keyboard', () => {
    login.mockClear();
    component.form.setValue({ email: 'test@example.com', password: 'password' });
    fixture.detectChanges();

    const form: HTMLFormElement = fixture.nativeElement.querySelector('#loginForm');
    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));

    expect(login).toHaveBeenCalledOnce();
  });
});
