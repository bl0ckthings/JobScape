import { Component,Input } from '@angular/core';

@Component({
  selector: 'app-base-input',
  imports: [],
  templateUrl: './input.html',
  styleUrl: './input.css',
})
export class BaseInput {
  @Input() label = '';
  @Input() type: 'text' | 'email' | 'password' | 'url' | 'number' | 'tel' | 'date' = 'text';
  @Input() placeholder?: string;
  @Input() error?: string;
}
