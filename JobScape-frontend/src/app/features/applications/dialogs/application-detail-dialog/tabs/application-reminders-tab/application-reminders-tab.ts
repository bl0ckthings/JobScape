import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { DetailReminder } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-reminders-tab',
  imports: [DatePipe, DetailTag],
  templateUrl: './application-reminders-tab.html',
  styleUrl: '../../detail-list.css',
})
export class ApplicationRemindersTab {
  readonly reminders = input<DetailReminder[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailReminder>();
}
