import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationDetailDialog } from './application-detail-dialog';
import { createApplicationDetailDemo } from './application-detail.demo';

@Component({
  selector: 'app-application-detail-preview',
  template: `
    <main class="preview">
      <div class="preview-card">
        <span class="eyebrow">JOBSCAPE · APERÇU LOCAL</span>
        <h1>Détail d’une candidature</h1>
        <p>Explorez les onglets et les formulaires de la maquette.</p>
        <p class="note">
          Données de démonstration. Les changements restent dans cet aperçu, sans enregistrement sur
          le serveur.
        </p>
        <button type="button" (click)="open()">Ouvrir le détail</button>
      </div>
    </main>
  `,
  styles: [
    `
      .preview {
        min-height: 100dvh;
        display: grid;
        place-items: center;
        background: #f8fafc;
        padding: 24px;
      }
      .preview-card {
        max-width: 520px;
        padding: 36px;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        background: white;
      }
      .eyebrow {
        font-size: 11px;
        letter-spacing: 0.1em;
        color: #3478f6;
        font-weight: 700;
      }
      h1 {
        font-size: 26px;
        margin: 16px 0 12px;
        font-weight: 700;
      }
      p {
        color: #64748b;
        font-size: 14px;
        line-height: 1.7;
      }
      .note {
        margin-top: 16px;
        font-size: 12px;
      }
      button {
        margin-top: 24px;
        background: #3478f6;
        color: white;
        padding: 12px 18px;
        border-radius: 10px;
        font-weight: 600;
      }
    `,
  ],
})
export class ApplicationDetailPreview {
  private readonly dialog = inject(MatDialog);
  open(): void {
    this.dialog.open(ApplicationDetailDialog, {
      width: '600px',
      maxWidth: '100vw',
      maxHeight: 'calc(100dvh - 48px)',
      panelClass: ['jobscape-dialog-panel', 'jobscape-detail-dialog-panel'],
      backdropClass: 'jobscape-dialog-backdrop',
      ariaLabelledBy: 'application-detail-title',
      data: createApplicationDetailDemo(),
    });
  }
}
