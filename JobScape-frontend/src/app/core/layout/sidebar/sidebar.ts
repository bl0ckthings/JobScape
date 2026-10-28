import { NgClass } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  ApplicationFormDialog, ApplicationFormValue
} from '../../../features/applications/dialogs/application-form-dialog/application-form-dialog';
import {
  ApplicationsBoardPage
} from '../../../features/applications/pages/applications-board-page/applications-board-page';
import { ApplicationDialogService } from '../../../features/applications/services/application-dialog.service';
import { ApplicationService } from '../../../features/applications/services/application.service';
import { filter, switchMap } from 'rxjs';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgClass, RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private readonly applicationDialog = inject(ApplicationDialogService);

  private readonly applicationService = inject(ApplicationService);

  openCreateApplicationDialog(): void {
    this.applicationDialog.openCreate()
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

  isOpen = false;

  toggleSidebar(event: Event): void {
    event?.preventDefault();
    this.isOpen = !this.isOpen;
  }
}
