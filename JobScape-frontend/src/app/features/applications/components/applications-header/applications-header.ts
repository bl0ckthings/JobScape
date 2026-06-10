import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-applications-header',
  imports: [],
  templateUrl: './applications-header.html',
  styleUrl: './applications-header.css',
})
export class ApplicationsHeader {

  userNotis = signal(15);
}
