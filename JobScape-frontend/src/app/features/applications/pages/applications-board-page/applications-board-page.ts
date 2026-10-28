import { Component, inject, model, OnInit, signal } from '@angular/core';
import { ApplicationsHeader } from '../../components/applications-header/applications-header';
import { StatusColumn } from '../../components/applications-status-column/status-column';
import { DateInput } from '../../../../shared/ui/date-input/date-input';
import { Badge } from '../../../../shared/ui/badge/badge';
import { BaseInput } from '../../../../shared/ui/base-input/input';
import { BaseSelect } from '../../../../shared/ui/base-select/base-select';
import { BaseTextarea } from '../../../../shared/ui/base-textarea/base-textarea';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ApplicationDialogService } from '../../services/application-dialog.service';
import { ApplicationService } from '../../services/application.service';
import { ApplicationFormValue } from '../../dialogs/application-form-dialog/application-form-dialog';
import { filter, switchMap } from 'rxjs';
import { ApplicationCategory, ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';

export interface DialogData {
  animal: string;
  name: string;
}

@Component({
  selector: 'app-applications-board-page',
  standalone: true,
  imports: [
    ApplicationsHeader,
    StatusColumn,
    DateInput,
    Badge,
    BaseInput,
    BaseSelect,
    BaseTextarea,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
  ],
  templateUrl: './applications-board-page.html',
  styleUrl: './applications-board-page.css',
})
export class ApplicationsBoardPage implements OnInit {
  readonly animal = signal('');
  readonly name = model('');
  private readonly applicationDialog = inject(ApplicationDialogService);
  private readonly applicationService = inject(ApplicationService);
  readonly columns: { category: ApplicationCategory; title: string }[] = [
    { category: 'WISHLIST', title: 'Wishlist' },
    { category: 'APPLIED', title: 'Envoyé' },
    { category: 'INTERVIEW', title: 'Entretien' },
  ];
  readonly applicationsByStatus = signal<Partial<Record<ApplicationCategory, ApplicationDto[]>>>({});
  readonly loadError = signal(false);

  ngOnInit(): void {
    this.loadApplications();
  }

  private loadApplications(): void {
    this.applicationService.getApplicationByUserId().subscribe({
      next: response => {
        this.applicationsByStatus.set(response.applicationsByStatus);
        this.loadError.set(false);
      },
      error: () => this.loadError.set(true),
    });
  }

  openCreateApplicationDialog(): void {
    this.applicationDialog.openCreate().afterClosed().pipe(
      // Une fermeture sans valeur signifie que l'utilisateur a annulé le formulaire.
      filter((value): value is ApplicationFormValue => value !== undefined),
      // Transforme les données du formulaire en requête HTTP, sans souscription imbriquée.
      switchMap(value => this.applicationService.createNewApplication(value)),
    ).subscribe({
      // La souscription déclenche la requête HTTP et reçoit son résultat.
      next: createdApplication => {
        this.loadApplications();
      },
      error: error => {
        // Afficher une erreur à l'utilisateur si la création échoue.
        console.error('Création échouée :', error);

      },
    });
  }
}
