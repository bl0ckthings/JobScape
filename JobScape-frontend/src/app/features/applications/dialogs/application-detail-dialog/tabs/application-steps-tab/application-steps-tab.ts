import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { DetailContact, DetailStep } from '../../application-detail.models';
import { DetailTag } from '../../components/detail-tag';
@Component({
  selector: 'app-application-steps-tab',
  imports: [DatePipe, DetailTag],
  templateUrl: './application-steps-tab.html',
  styleUrl: '../../detail-list.css',
})
export class ApplicationStepsTab {
  readonly steps = input<DetailStep[]>([]);
  readonly contacts = input<DetailContact[]>([]);
  readonly added = output<void>();
  readonly edited = output<DetailStep>();
  contactName(id: number | null): string {
    return this.contacts().find((item) => item.id === id)?.name ?? '';
  }
}
