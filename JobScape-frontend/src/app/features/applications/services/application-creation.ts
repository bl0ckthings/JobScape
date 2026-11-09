import { inject, Injectable } from '@angular/core';
import { ApplicationDialogService } from './application-dialog.service';
import { ApplicationService } from './application.service';
import { filter, Subject, switchMap } from 'rxjs';
import { ApplicationFormValue } from '../dialogs/application-form-dialog/application-form-dialog';

@Injectable({
  providedIn: 'root',
})
export class ApplicationCreation {
  private readonly applicationDialogService: ApplicationDialogService =
    inject(ApplicationDialogService);
  private readonly applicationService: ApplicationService = inject(ApplicationService);

  // Seul ce service peut émettre l'événement.
  private readonly applicationCreated = new Subject<void>();

  // Les autres composants peuvent seulement l'écouter.
  readonly created$ = this.applicationCreated.asObservable();

  openCreateApplicationDialog(): void {
    this.applicationDialogService
      .openCreate()
      .afterClosed()
      .pipe(
        // Annulation : on ne crée rien.
        filter((value): value is ApplicationFormValue => value !== undefined),
        switchMap((value) => this.applicationService.createNewApplication(value)),
      )
      .subscribe({
        next: () => {
          // On prévient le tableau après la création.
          this.applicationCreated.next();
        },
        error: (error) => {
          console.error('Création échouée :', error);

        },
      });
  }
}
