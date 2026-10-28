import { Component, inject, input } from '@angular/core';
import { ApplicationCard } from '../application-card/application-card';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { ApplicationDialogService } from '../../services/application-dialog.service';
import { ApplicationService } from '../../services/application.service';
import { filter, switchMap } from 'rxjs';
import { ApplicationFormValue } from '../../dialogs/application-form-dialog/application-form-dialog';

@Component({
  selector: 'app-applications-status-column',
  imports: [ApplicationCard],
  templateUrl: './status-column.html',
  styleUrl: './status-column.css',
})
export class StatusColumn {
  readonly title = input('Wishlist');
  readonly applications = input<ApplicationDto[]>([]);

  private readonly applicationDialog = inject(ApplicationDialogService);

  private readonly applicationService = inject(ApplicationService);

  openCreateApplicationDialog(): void {
    this.applicationDialog
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
          console.log(createdApplication);
          console.log('ID :', createdApplication.id);
        },
        error: (error) => {
          // Afficher une erreur à l'utilisateur si la création échoue.
          console.error('Création échouée :', error);
        },
      });
  }
}
