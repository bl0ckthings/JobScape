import { Component } from '@angular/core';
import { ApplicationsHeader } from '../../components/applications-header/applications-header';
import { SortFilterBar } from '../../components/sort-filter-bar/sort-filter-bar';
import { Board } from '../../components/board/board';
import { ApplicationCardMenu } from '../../components/application-card-menu/application-card-menu';
import { ApplicationCard } from '../../components/application-card/application-card';
import { StatusColumn } from '../../components/status-column/status-column';

@Component({
  selector: 'app-applications-board-page',
  imports: [
    ApplicationsHeader,
    SortFilterBar,
    Board,
    StatusColumn,
    ApplicationCardMenu,
    ApplicationCard
  ],
  templateUrl: './applications-board-page.html',
  styleUrl: './applications-board-page.css',
})
export class ApplicationsBoardPage {}
