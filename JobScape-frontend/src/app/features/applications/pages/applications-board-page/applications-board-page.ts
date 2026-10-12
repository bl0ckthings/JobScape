import { Component, inject, model, signal } from '@angular/core';
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
export class ApplicationsBoardPage {
  readonly animal = signal('');
  readonly name = model('');
  private readonly applicationDialog = inject(ApplicationDialogService);

  openCreateApplicationDialog(): void {
    this.applicationDialog.openCreate();
  }
}
