import { Component, inject, input } from '@angular/core';
import { ApplicationCard } from '../application-card/application-card';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { ApplicationDialogService } from '../../services/application-dialog.service';
import { ApplicationService } from '../../services/application.service';
import { filter, switchMap } from 'rxjs';
import { ApplicationFormValue } from '../../dialogs/application-form-dialog/application-form-dialog';
import { ApplicationCreation } from '../../services/application-creation';

@Component({
  selector: 'app-applications-status-column',
  imports: [ApplicationCard],
  templateUrl: './status-column.html',
  styleUrl: './status-column.css',
})
export class StatusColumn {
  readonly title = input('Wishlist');
  readonly applications = input<ApplicationDto[]>([]);

  private readonly applicationCreation = inject(ApplicationCreation);

  openCreateApplicationDialog(): void {
   this.applicationCreation.openCreateApplicationDialog();
  }
}
