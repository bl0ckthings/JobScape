import { Component, input } from '@angular/core';
import { DetailTone } from '../application-detail.models';

@Component({
  selector: 'app-detail-tag',
  template: `<span class="tag" [attr.data-tone]="tone()"><ng-content /></span>`,
  styles: [
    `
      :host {
        display: inline-flex;
      }
      .tag {
        width: 100%;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 999px;
        padding: 5px 12px;
        font-size: 11px;
        font-weight: 600;
        line-height: 1.3;
        white-space: nowrap;
      }
      [data-tone='blue'] {
        color: #3478f6;
        background: #eff6ff;
      }
      [data-tone='green'] {
        color: #087b63;
        background: #e9fbf3;
      }
      [data-tone='purple'] {
        color: #7c3aed;
        background: #faf5ff;
      }
      [data-tone='amber'] {
        color: #b45f06;
        background: #fff8e7;
      }
      [data-tone='neutral'] {
        color: #0f172a;
        background: #f8fafc;
      }
    `,
  ],
})
export class DetailTag {
  readonly tone = input<DetailTone>('blue');
}
