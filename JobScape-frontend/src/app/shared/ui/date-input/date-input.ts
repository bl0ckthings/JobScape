import { Component, Input } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';

@Component({
  selector: 'app-date-input',
  imports: [MatDatepickerModule, MatNativeDateModule, MatIconModule, MatInput],
  templateUrl: './date-input.html',
  styleUrl: './date-input.css',
})
export class DateInput {
  @Input() label = '';
  @Input() placeholder = 'Choisir une date ';
  @Input() error?: string;
}
