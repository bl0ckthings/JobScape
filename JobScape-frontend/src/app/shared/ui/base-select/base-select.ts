import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-base-select',
  imports: [],
  templateUrl: './base-select.html',
  styleUrl: './base-select.css',
})
export class BaseSelect {
  @Input() label = '';
  @Input() values: string[] = [];
  @Input() placeholder = 'Sélectionner une option';
  @Input() error?: string;
  @Input() name = '';
}
