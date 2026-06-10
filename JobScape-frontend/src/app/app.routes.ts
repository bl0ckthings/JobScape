import { Routes } from '@angular/router';
import { ApplicationsBoardPage } from './features/applications/pages/applications-board-page/applications-board-page';
import { AppShell } from './core/layout/app-shell/app-shell';

export const routes: Routes = [
  
      {
        path: 'applications',
        component: ApplicationsBoardPage,
      },
      {
        path: 'dashboard',
        component: ApplicationsBoardPage, // replace with DashboardPage later
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },

];
