import { Component, DestroyRef, inject, model, OnInit, signal } from '@angular/core';
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
import { ApplicationCreation } from '../../services/application-creation';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

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
