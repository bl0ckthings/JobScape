import { Component, signal } from '@angular/core';
import { AppShell } from "./core/layout/app-shell/app-shell";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AppShell, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('jobTracking');
}
