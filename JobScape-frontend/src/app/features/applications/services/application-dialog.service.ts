import { inject, Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import {
  ApplicationFormDialog,
  ApplicationFormDialogData,
  ApplicationFormValue,
} from '../dialogs/application-form-dialog/application-form-dialog';

import { ApplicationDetailDialog } from '../dialogs/application-detail-dialog/application-detail-dialog';
import { ApplicationDetailData } from '../dialogs/application-detail-dialog/application-detail.models';
import {
  ApplicationDeleteDialog,
  ApplicationDeleteDialogData,
} from '../dialogs/application-delete-dialog/application-delete-dialog';

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
  openEdit(
    application: NonNullable<ApplicationFormDialogData['application']>,
  ): ApplicationDialogRef {
    return this.open({ mode: 'edit', application });
  }

  /** Close the currently open dialog, optionally returning a value to the caller. */
  close(result?: ApplicationFormValue): void {
    this.dialogRef?.close(result);
  }

  private open(data: ApplicationFormDialogData): ApplicationDialogRef {
    this.dialogRef = this.dialog.open(ApplicationFormDialog, {
      width: '600px',
      maxWidth: 'calc(100vw - 24px)',
      maxHeight: 'calc(100dvh - 24px)',
      panelClass: 'jobscape-dialog-panel',
      backdropClass: 'jobscape-dialog-backdrop',
      disableClose: false,
      data,
    });

    // Drop the reference once closed so we never hold a stale ref.
    this.dialogRef.afterClosed().subscribe(() => (this.dialogRef = undefined));

    return this.dialogRef;
  }

  openDetail(data: ApplicationDetailData): MatDialogRef<ApplicationDetailDialog, void> {
    return this.dialog.open<ApplicationDetailDialog, ApplicationDetailData, void>(
      ApplicationDetailDialog,
      {
        width: '600px',
        maxWidth: '100vw',
        maxHeight: 'calc(100dvh - 48px)',
        panelClass: [
          'jobscape-dialog-panel',
          'jobscape-detail-dialog-panel',
          'detail-mobile:w-screen!',
          'detail-mobile:max-w-[100vw]!',
          'detail-mobile:h-dvh',
          'detail-mobile:max-h-dvh!',
          'detail-mobile:rounded-none',
          'detail-mobile:[&_.mat-mdc-dialog-surface]:rounded-none!',
        ],
        backdropClass: 'jobscape-dialog-backdrop',
        ariaLabelledBy: 'application-detail-title',
        data,
      },
    );
  }

  openDelete(
    data: ApplicationDeleteDialogData,
  ): MatDialogRef<ApplicationDeleteDialog, boolean> {
    return this.dialog.open<ApplicationDeleteDialog, ApplicationDeleteDialogData, boolean>(
      ApplicationDeleteDialog,
      {
        width: '420px',
        maxWidth: 'calc(100vw - 24px)',
        panelClass: 'jobscape-dialog-panel',
        backdropClass: 'jobscape-dialog-backdrop',
        ariaLabelledBy: 'application-delete-title',
        data,
      },
    );
  }
}
