import { Component, input } from '@angular/core';

// Adapté du spinner animate-spin : https://tailwindcss.com/docs/animation#adding-a-spin-animation
@Component({
  selector: 'app-spinner',
  host: { class: 'inline-flex shrink-0' },
  template: `
    <span role="status" class="inline-flex items-center justify-center">
      <svg
        class="size-6 text-blue-600 motion-safe:animate-spin"
        viewBox="0 0 24 24"
        fill="none"
        aria-hidden="true"
      >
        <circle class="opacity-20" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" />
        <path
          d="M12 2a10 10 0 0 1 10 10"
          stroke="currentColor"
          stroke-width="3"
          stroke-linecap="round"
        />
      </svg>
      <span class="sr-only">{{ label() }}</span>
    </span>
  `,
})
export class Spinner {
  readonly label = input('Chargement…');
}
