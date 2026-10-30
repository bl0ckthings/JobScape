import { Component, computed, ElementRef, HostListener, inject, input, signal,DestroyRef } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { Badge } from '../../../../shared/ui/badge/badge';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize, map } from 'rxjs';
import { ApplicationService } from '../../services/application.service';
import { ApplicationDialogService } from '../../services/application-dialog.service';
import { toApplicationDetailData } from '../../dialogs/application-detail-dialog/application-detail.mapper';
import { disabled } from '@angular/forms/signals';
@Component({
  selector: 'app-application-card',
  imports: [DatePipe, Badge],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {
  private readonly element: ElementRef<HTMLElement> = inject(ElementRef);
  private readonly applicationService = inject(ApplicationService);
  private readonly applicationDialog = inject(ApplicationDialogService);
  private readonly destroyRef = inject(DestroyRef);
  readonly detailLoading = signal(false);
  readonly detailError = signal<string | null>(null);
  readonly actionsOpen = signal(false);
  readonly application = input<ApplicationDto | null>(null);
  readonly companyName = input('');
  readonly companyDomain = input<string | null>(null);
  readonly heading = computed(
    () => this.application()?.companyName?.trim() || this.companyName().trim() || 'Entreprise',
  );
  readonly logoUrl = computed(() => {
    const application = this.application();
    const storedLogo = application?.companyLogoUrl?.trim();
    if (storedLogo && !storedLogo.startsWith('https://cdn.brandfetch.io/')) return storedLogo;

    const domain = (application ? application.companyDomain : this.companyDomain())?.trim();
    const clientId = environment.brandFetch_clientId.trim();

    return domain && clientId
      ? `https://cdn.brandfetch.io/domain/${encodeURIComponent(domain)}/w/28/h/28/fallback/lettermark?c=${encodeURIComponent(clientId)}`
      : null;
  });

  toggleActions(): void {
    this.actionsOpen.update((open) => !open);
  }

  closeActions(): void {
    this.actionsOpen.set(false);
  }

  openDetail(): void {
    const application = this.application();

    if (!application || this.detailLoading()) {
      return;
    }

    this.closeActions();
    this.detailLoading.set(true);
    this.detailError.set(null);

    this.applicationService
      .getApplicationDetail(application.id)
      .pipe(
        // Adapte la réponse au modèle attendu par le modal.
        map((response) => toApplicationDetailData(response)),

        // Arrête l'attente après réussite, erreur ou annulation.
        finalize(() => this.detailLoading.set(false)),

        // Annule la requête si Angular détruit cette carte.
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (data) => {
          this.applicationDialog.openDetail(data);
        },

        error: (error: unknown) => {
          if (error instanceof HttpErrorResponse && error.status === 404) {
            this.detailError.set('Cette candidature est introuvable.');
            return;
          }

          if (
            error instanceof HttpErrorResponse &&
            (error.status === 401 || error.status === 403)
          ) {
            this.detailError.set('Reconnecte-toi pour accéder à cette candidature.');
            return;
          }

          this.detailError.set('Impossible de charger les détails. Réessaie dans un instant.');
        },
      });
  }

  @HostListener('document:click', ['$event'])
  closeOnOutsideClick(event: MouseEvent): void {
    if (!this.element.nativeElement.contains(event.target as Node)) this.closeActions();
  }

  @HostListener('document:keydown.escape')
  closeOnEscape(): void {
    this.closeActions();
  }

  protected readonly disabled = disabled;
}
