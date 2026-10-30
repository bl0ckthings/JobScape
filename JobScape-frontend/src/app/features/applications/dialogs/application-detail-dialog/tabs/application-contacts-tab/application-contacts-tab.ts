import { Component, input, output } from '@angular/core';
import { DetailContact } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-contacts-tab',
  host: { class: 'block' },
  imports: [DetailTag],
  templateUrl: './application-contacts-tab.html',
})
export class ApplicationContactsTab {
  readonly contacts = input<DetailContact[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailContact>()
  readonly editable = input(true);
}
