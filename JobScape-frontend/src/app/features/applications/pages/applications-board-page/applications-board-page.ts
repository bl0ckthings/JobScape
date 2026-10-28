import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { ApplicationsHeader } from '../../components/applications-header/applications-header';
import { StatusColumn } from '../../components/applications-status-column/status-column';
import { ApplicationService } from '../../services/application.service';
import { ApplicationCategory, ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { ApplicationCreation } from '../../services/application-creation';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-applications-board-page',
  standalone: true,
  imports: [ApplicationsHeader, StatusColumn],
  templateUrl: './applications-board-page.html',
  styleUrl: './applications-board-page.css',
})
export class ApplicationsBoardPage implements OnInit {
  private readonly applicationService = inject(ApplicationService);
  private readonly applicationCreation = inject(ApplicationCreation);
  private readonly destroyRef = inject(DestroyRef);

  readonly columns: { category: ApplicationCategory; title: string }[] = [
    { category: 'WISHLIST', title: 'Wishlist' },
    { category: 'APPLIED', title: 'Envoyé' },
    { category: 'INTERVIEW', title: 'Entretien' },
  ];
  readonly applicationsByStatus = signal<Partial<Record<ApplicationCategory, ApplicationDto[]>>>(
    {},
  );
  readonly loadError = signal(false);

  ngOnInit(): void {
    this.applicationCreation.created$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.loadApplications());

    this.loadApplications();
  }

  private loadApplications(): void {
    this.applicationService.getApplicationByUserId().subscribe({
      next: (response) => {
        this.applicationsByStatus.set(response.applicationsByStatus);
        this.loadError.set(false);
      },
      error: () => this.loadError.set(true),
    });
  }

  openCreateApplicationDialog(): void {
    this.applicationCreation.openCreateApplicationDialog();
  }
}
