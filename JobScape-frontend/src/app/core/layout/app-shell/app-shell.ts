import { Component } from '@angular/core';
import { Sidebar } from "../sidebar/sidebar";
import {RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-app-shell',
  standalone: true,
  imports: [Sidebar, RouterOutlet],
  templateUrl: './app-shell.html',
  styleUrl: './app-shell.css',
})
export class AppShell {
  
}
