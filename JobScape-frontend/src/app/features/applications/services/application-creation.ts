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
        // Une fermeture sans valeur signifie que l'utilisateur a annulé le formulaire.
        filter((value): value is ApplicationFormValue => value !== undefined),
        // Transforme les données du formulaire en requête HTTP, sans souscription imbriquée.
        switchMap((value) => this.applicationService.createNewApplication(value)),
      )
      .subscribe({
        // La souscription déclenche la requête HTTP et reçoit son résultat.
        next: (createdApplication) => {
          // Actualiser les candidatures quand le tableau utilisera les données de l'API.
          this.applicationCreated.next();
        },
        error: (error) => {
          // Afficher une erreur à l'utilisateur si la création échoue.
          console.error('Création échouée :', error);

        },
      });
  }
}
