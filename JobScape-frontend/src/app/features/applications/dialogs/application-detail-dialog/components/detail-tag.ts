import { Component, input } from '@angular/core';
import { DetailTone } from '../application-detail.models';

@Component({
  selector: 'app-detail-tag',
  host: { class: 'inline-flex' },
  template: `<span
    class="tag w-full inline-flex items-center justify-center rounded-[999px] py-[5px] px-3 text-[11px] font-semibold leading-[1.3] whitespace-nowrap data-[tone=blue]:text-detail-blue data-[tone=blue]:bg-[#eff6ff] data-[tone=green]:text-[#087b63] data-[tone=green]:bg-[#e9fbf3] data-[tone=purple]:text-[#7c3aed] data-[tone=purple]:bg-[#faf5ff] data-[tone=amber]:text-[#b45f06] data-[tone=amber]:bg-[#fff8e7] data-[tone=neutral]:text-detail-ink data-[tone=neutral]:bg-detail-surface"
    [attr.data-tone]="tone()"
    ><ng-content
  /></span>`,
})
export class DetailTag {
  readonly tone = input<DetailTone>('blue');
}
