import {
  Component,
  computed,
  ElementRef,
  HostListener,
  inject,
  input,
  output,
  signal,
  DestroyRef,
} from '@angular/core';
import { getCompanyLogoUrl } from '../../company-logo';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { Company } from '../../../../shared/models/applications/company.model';
import { Badge } from '../../../../shared/ui/badge/badge';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize, map, filter, switchMap } from 'rxjs';
import { ApplicationService } from '../../services/application.service';
import { ApplicationDialogService } from '../../services/application-dialog.service';
import { ApplicationFormValue } from '../../dialogs/application-form-dialog/application-form-dialog';
import { toApplicationDetailData } from '../../dialogs/application-detail-dialog/application-detail.mapper';
import { Spinner } from '../../../../shared/ui/spinner/spinner';
import { disabled } from '@angular/forms/signals';
@Component({
  selector: 'app-application-card',
  imports: [DatePipe, Badge, Spinner],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {
  private readonly element: ElementRef<HTMLElement> = inject(ElementRef);
  private readonly applicationService = inject(ApplicationService);
  private readonly applicationDialog = inject(ApplicationDialogService);
  private readonly destroyRef = inject(DestroyRef);
  readonly detailLoading = signal(false);
  readonly deleteLoading = signal(false);
  readonly updateLoading = signal(false);
  readonly applicationUpdated = output<void>();
  readonly detailError = signal<string | null>(null);
  readonly actionsOpen = signal(false);
  readonly applicationDeleted = output<void>();
  readonly application = input<ApplicationDto | null>(null);
  readonly company = input<Company | null>(null);
  readonly companyName = input('');
  readonly companyDomain = input<string | null>(null);
  readonly heading = computed(
    () => this.company()?.name?.trim() || this.companyName().trim() || 'Entreprise',
  );
  readonly logoUrl = computed(() => {
    const company = this.company();
    return getCompanyLogoUrl(
      company?.logoUrl,
      company?.domain ?? this.companyDomain(),
    );
  });

  toggleActions(): void {
    this.actionsOpen.update((open) => !open);
  }

  closeActions(): void {
    this.actionsOpen.set(false);
  }

  deleteApplication(): void {
    const application = this.application();
    if (!application || this.deleteLoading()) {
      return;
    }

    this.closeActions();
    this.detailError.set(null);

    this.applicationDialog
      .openDelete({
        companyName: this.heading(),
        jobTitle: application.jobTitle,
      })
      .afterClosed()
      .pipe(
        filter((confirmed): confirmed is true => confirmed === true),
        switchMap(() => {
          this.deleteLoading.set(true);

          return this.applicationService
            .deleteApplication(application.id)
            .pipe(finalize(() => this.deleteLoading.set(false)));
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: () => {
          this.applicationDeleted.emit();
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

          this.detailError.set('Impossible de supprimer la candidature. Réessaie dans un instant.');
        },
      });
  }

  updateApplication(): void {
    const application = this.application();

    const company = this.company();

    if (!application || this.updateLoading()) {
      return;
    }

    this.closeActions();
    this.detailError.set(null);
    this.applicationDialog
      .openEdit({
      id: application.id,
      companyName: company?.name ?? this.companyName(),
      jobTitle: application.jobTitle,
      city: application.city,
      country: application.country,
      source: application.source,
      workMode: application.workMode,
      url: application.url ?? '',
      initialStatus: application.status
    })
      .afterClosed()
      .pipe(
        filter((value): value is ApplicationFormValue => value !== undefined),

        map((value): ApplicationDto => ({
          ...application,
          jobTitle: value.jobTitle,
          city: value.city,
          country: value.country,
          source: value.source,
          workMode: value.workMode,
          url: value.url || null,
          status: value.initialStatus
        })),
        switchMap((updatedApplication) => {
          this.updateLoading.set(true);

          return this.applicationService
          .updateApplication(updatedApplication)
          .pipe(finalize(() => this.updateLoading.set(false)));
        }),

        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: () => {
          this.applicationUpdated.emit();
        },
        error: () => {
          this.detailError.set(
            "Impossible de modifier la candidature."
          )
        }
      })




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
