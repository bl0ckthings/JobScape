import { TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs';

import { ApplicationDialogService } from './application-dialog.service';

describe('ApplicationDialogService', () => {
  it('keeps the creation dialog within the mobile viewport', () => {
    const open = vi.fn(() => ({ afterClosed: () => of(undefined) }));
    TestBed.configureTestingModule({
      providers: [ApplicationDialogService, { provide: MatDialog, useValue: { open } }],
    });

    TestBed.inject(ApplicationDialogService).openCreate();

    expect(open).toHaveBeenCalledWith(expect.anything(), expect.objectContaining({
      maxWidth: 'calc(100vw - 24px)',
      maxHeight: 'calc(100dvh - 24px)',
    }));
  });
});
