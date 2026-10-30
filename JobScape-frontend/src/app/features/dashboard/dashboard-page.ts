import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  template: `
    <main class="mt-header-height min-h-full bg-[#F7F8FC] px-4 py-8 sm:px-6 lg:mt-0 lg:p-8">
      <h1 class="text-2xl font-bold text-slate-950">Dashboard</h1>
      <p class="mt-3 text-slate-500">Le tableau de bord arrive bientôt. Retrouve tes candidatures dans l’onglet Candidatures.</p>
      <a routerLink="/applications" class="mt-6 inline-flex rounded-xl bg-primary px-5 py-3 font-semibold text-white">Voir mes candidatures</a>
    </main>
  `,
  imports: [RouterLink],
})
export class DashboardPage {}
