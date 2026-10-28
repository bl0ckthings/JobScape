import { Component, ElementRef, HostListener, inject, input, signal } from '@angular/core';
import { FormControl } from '@angular/forms';

export type FormSelectOption = { value: string; label: string };

@Component({
  selector: 'app-application-form-select',
  standalone: true,
  templateUrl: './application-form-select.html',
})
export class ApplicationFormSelect {
  private readonly element: ElementRef<HTMLElement> = inject(ElementRef);

  readonly selectId = input.required<string>();
  readonly control = input.required<FormControl<string>>();
  readonly options = input.required<readonly FormSelectOption[]>();
  readonly placeholder = input('Sélectionner une option');
  readonly dropUp = input(false);
  readonly open = signal(false);

  selectedLabel(): string {
    return this.options().find(option => option.value === this.control().value)?.label ?? this.placeholder();
  }

  toggle(): void {
    if (!this.control().disabled) this.open.update(open => !open);
  }

  choose(option: FormSelectOption): void {
    this.control().setValue(option.value);
    this.control().markAsTouched();
    this.open.set(false);
    this.element.nativeElement.querySelector<HTMLButtonElement>('.form-select-trigger')?.focus();
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.open.set(false);
      this.element.nativeElement.querySelector<HTMLButtonElement>('.form-select-trigger')?.focus();
      return;
    }
    if (event.key !== 'ArrowDown' && event.key !== 'ArrowUp') return;

    event.preventDefault();
    if (!this.open()) this.open.set(true);
    queueMicrotask(() => {
      const choices = Array.from(this.element.nativeElement.querySelectorAll<HTMLButtonElement>('[role="option"]'));
      const current = choices.indexOf(document.activeElement as HTMLButtonElement);
      const next = event.key === 'ArrowDown'
        ? (current + 1) % choices.length
        : (current - 1 + choices.length) % choices.length;
      choices[next]?.focus();
    });
  }

  @HostListener('document:click', ['$event'])
  closeOnOutsideClick(event: MouseEvent): void {
    if (!this.element.nativeElement.contains(event.target as Node)) this.open.set(false);
  }
}
