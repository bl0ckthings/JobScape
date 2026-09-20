import { NgClass } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  ApplicationFormDialog
} from '../../../features/applications/dialogs/application-form-dialog/application-form-dialog';
import {
  ApplicationsBoardPage
} from '../../../features/applications/pages/applications-board-page/applications-board-page';
import { ApplicationDialogService } from '../../../features/applications/services/application-dialog.service';

@Component({
  selector: 'app-sidebar',
    standalone: true,
  imports: [NgClass, RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private openCloseForm:ApplicationDialogService = inject(ApplicationDialogService);

  openForm() {
    this.openCloseForm.openCreate()
  }



isOpen = false;

toggleSidebar(event: Event):void {
  event?.preventDefault();
  this.isOpen = !this.isOpen;
}


}
