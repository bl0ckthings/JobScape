import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-sidebar',
    standalone: true,
  imports: [NgClass, RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {


isOpen = false;

toggleSidebar(event: Event):void {
  event?.preventDefault();
  this.isOpen = !this.isOpen;
}


}
