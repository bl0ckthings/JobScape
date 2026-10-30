import { Component, input, output } from '@angular/core';
import { DetailContact } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-contacts-tab',
  imports: [DetailTag],
  templateUrl: './application-contacts-tab.html',
  styleUrl: '../../detail-list.css',
})
export class ApplicationContactsTab {
  readonly contacts = input<DetailContact[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailContact>();
}
