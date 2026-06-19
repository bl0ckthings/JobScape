import { Component } from '@angular/core';
import { ApplicationsHeader } from '../../components/applications-header/applications-header';
import { StatusColumn } from '../../components/status-column/status-column';

@Component({
  selector: 'app-applications-board-page',
  imports: [
    ApplicationsHeader,
    StatusColumn,
  ],
  templateUrl: './applications-board-page.html',
  styleUrl: './applications-board-page.css',
})
export class ApplicationsBoardPage {}
