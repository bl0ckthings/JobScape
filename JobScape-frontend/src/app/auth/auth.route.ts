import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';

export const AUTH_ROUTES: Routes = [
  { path: 'login',
    loadComponent: () => import('./login/login').then(m => m.Login), },
  {
    path: 'register',
    loadComponent: () => import('./register/register').then((m) => m.Register),
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
];
