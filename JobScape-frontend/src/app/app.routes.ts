import { Routes } from '@angular/router';
import { ApplicationsBoardPage } from './features/applications/pages/applications-board-page/applications-board-page';
import { AppShell } from './core/layout/app-shell/app-shell';
import { authGuard } from './core/guards/auth-guard';


export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.route').then((m) => m.AUTH_ROUTES),
  },
  {
    path: '',
    canActivate: [authGuard],
    component: AppShell,
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/applications/pages/applications-board-page/applications-board-page').then(
            (m) => m.ApplicationsBoardPage,
          ),
      },

      // Ici toutes les routes qui vont détenir ma sidebar
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
