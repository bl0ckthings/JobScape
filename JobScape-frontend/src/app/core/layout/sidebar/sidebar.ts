import { NgClass } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ApplicationCreation } from '../../../features/applications/services/application-creation';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgClass, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {

  private readonly applicationCreate = inject(ApplicationCreation);

  openCreateApplicationDialog(): void {
   this.applicationCreate.openCreateApplicationDialog();
  }

  isOpen = false;

  toggleSidebar(event: Event): void {
    event?.preventDefault();
    this.isOpen = !this.isOpen;
  }

  closeSidebar(): void {
    this.isOpen = false;
  }
}
