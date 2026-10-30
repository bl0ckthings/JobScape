import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { DetailReminder } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-reminders-tab',
  host: { class: 'block' },
  imports: [DatePipe, DetailTag],
  templateUrl: './application-reminders-tab.html',
})
export class ApplicationRemindersTab {
  readonly reminders = input<DetailReminder[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailReminder>();
  readonly editable = input(true);
}
