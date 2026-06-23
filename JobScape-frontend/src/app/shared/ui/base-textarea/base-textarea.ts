import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-base-textarea',
  imports: [],
  templateUrl: './base-textarea.html',
  styleUrl: './base-textarea.css',
})
export class BaseTextarea {
  @Input() label = '';
  @Input() placeholder:string = '';
  @Input() rows = 3;
  @Input() optional = false;
  @Input() error?: string;
}
