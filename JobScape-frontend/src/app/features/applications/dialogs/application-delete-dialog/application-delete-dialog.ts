import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface ApplicationDeleteDialogData {
  companyName: string;
  jobTitle: string;
}

@Component({
  selector: 'app-application-delete-dialog',
  imports: [],
  templateUrl: './application-delete-dialog.html',
  styleUrl: './application-delete-dialog.css',
})
export class ApplicationDeleteDialog {
  readonly data = inject<ApplicationDeleteDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<ApplicationDeleteDialog, boolean>>(MatDialogRef);

  cancel(): void {
    this.dialogRef.close(false);
  }

  confirm(): void {
    this.dialogRef.close(true);
  }
}
