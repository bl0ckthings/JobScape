import { Routes } from '@angular/router';
import { isDevMode } from '@angular/core';
import { AppShell } from './core/layout/app-shell/app-shell';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  // Aperçu de travail uniquement : aucune route ajoutée au parcours utilisateur en production.
  ...(isDevMode()
    ? [
        {
          path: 'preview/application-detail',
          loadComponent: () =>
            import('./features/applications/dialogs/application-detail-dialog/application-detail-preview').then(
              (m) => m.ApplicationDetailPreview,
            ),
        },
      ]
    : []),
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
        path: 'applications',
        loadComponent: () =>
          import('./features/applications/pages/applications-board-page/applications-board-page').then(
            (m) => m.ApplicationsBoardPage,
          ),
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard-page').then((m) => m.DashboardPage),
      },

      // Ici toutes les routes qui vont détenir ma sidebar
      {
        path: '',
        redirectTo: 'applications',
        pathMatch: 'full',
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
