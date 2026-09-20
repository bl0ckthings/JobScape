import { inject, Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import {
  ApplicationFormDialog,
  ApplicationFormDialogData,
  ApplicationFormValue,
} from '../dialogs/application-form-dialog/application-form-dialog';

type ApplicationDialogRef = MatDialogRef<ApplicationFormDialog, ApplicationFormValue | undefined>;

@Injectable({
  providedIn: 'root',
})
export class ApplicationDialogService {
  private readonly dialog = inject(MatDialog);

  /** Currently open dialog, if any. */
  private dialogRef?: ApplicationDialogRef;

  /** Open the form in "create" mode. */
  openCreate(): ApplicationDialogRef {
    return this.open({ mode: 'create' });
  }

  /** Open the form in "edit" mode, pre-filled with an existing application. */
  openEdit(application: NonNullable<ApplicationFormDialogData['application']>): ApplicationDialogRef {
    return this.open({ mode: 'edit', application });
  }

  /** Close the currently open dialog, optionally returning a value to the caller. */
  close(result?: ApplicationFormValue): void {
    this.dialogRef?.close(result);
  }

  private open(data: ApplicationFormDialogData): ApplicationDialogRef {
    this.dialogRef = this.dialog.open(ApplicationFormDialog, {
      width: '600px',
      maxWidth: 'calc(100vw - 32px)',
      panelClass: 'jobscape-dialog-panel',
      backdropClass: 'jobscape-dialog-backdrop',
      disableClose: false,
      data,
    });

    // Drop the reference once closed so we never hold a stale ref.
    this.dialogRef.afterClosed().subscribe(() => (this.dialogRef = undefined));

    return this.dialogRef;
  }
}
