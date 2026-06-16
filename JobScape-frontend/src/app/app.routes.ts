import { Routes } from '@angular/router';
import { ApplicationsBoardPage } from './features/applications/pages/applications-board-page/applications-board-page';
import { AppShell } from './core/layout/app-shell/app-shell';


export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.route').then((m) => m.AUTH_ROUTES),
  },
  {
    path: '',
    component: AppShell,
    children: [
      {
        path: 'applications',
        loadComponent: () =>
          import('./features/applications/pages/applications-board-page/applications-board-page').then(
            (m) => m.ApplicationsBoardPage,
          ),
      },

      // Add all pages that should display the sidebar here.
      {
        path: '',
        redirectTo: 'applications',
        pathMatch: 'full',
      },
    ],
  },
];
