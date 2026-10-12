import { Component } from '@angular/core';
import { ApplicationCard } from '../application-card/application-card';

@Component({
  selector: 'app-applications-status-column',
  imports: [ApplicationCard],
  templateUrl: './status-column.html',
  styleUrl: './status-column.css',
})
export class StatusColumn {}
