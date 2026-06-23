import { Component, inject } from '@angular/core';
import { Dialog } from '@angular/cdk/dialog';
import { ApplicationsHeader } from '../../components/applications-header/applications-header';
import { StatusColumn } from '../../components/applications-status-column/status-column';
import { DateInput } from '../../../../shared/ui/date-input/date-input';
import { Badge } from '../../../../shared/ui/badge/badge';
import { BaseInput } from '../../../../shared/ui/base-input/input';
import { BaseSelect } from '../../../../shared/ui/base-select/base-select';
import { BaseTextarea } from '../../../../shared/ui/base-textarea/base-textarea';
import { ApplicationFormDialog } from '../../dialogs/application-form-dialog/application-form-dialog';

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
  ],
  templateUrl: './applications-board-page.html',
  styleUrl: './applications-board-page.css',
})
export class ApplicationsBoardPage {
  private readonly dialog = inject(Dialog);

  openCreateApplicationDialog(): void {
    const dialogRef = this.dialog.open(ApplicationFormDialog, {
      width: '400px',
      panelClass: 'jobscape-dialog-panel',
      backdropClass: 'jobscape-dialog-backdrop',
      disableClose: false,
      data: {
        mode: 'create',
      },
    });

    dialogRef.closed.subscribe((result) => {
      if (!result) return;

      console.log('Create application payload:', result);
      // call your service here
    });
  }
}
