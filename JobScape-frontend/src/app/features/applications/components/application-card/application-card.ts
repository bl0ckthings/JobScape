import { Component, computed, ElementRef, HostListener, inject, input, signal } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { Badge } from '../../../../shared/ui/badge/badge';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-application-card',
  imports: [DatePipe, Badge],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {
  private readonly element: ElementRef<HTMLElement> = inject(ElementRef);
  readonly actionsOpen = signal(false);
  readonly application = input<ApplicationDto | null>(null);
  readonly companyName = input('');
  readonly companyDomain = input<string | null>(null);
  readonly heading = computed(() => this.application()?.companyName?.trim() || this.companyName().trim() || 'Entreprise');
  readonly logoUrl = computed(() => {
    const application = this.application();
    const storedLogo = application?.companyLogoUrl?.trim();
    if (storedLogo && !storedLogo.startsWith('https://cdn.brandfetch.io/')) return storedLogo;

    const domain = (application ? application.companyDomain : this.companyDomain())?.trim();
    const clientId = environment.brandFetch_clientId.trim();

    return domain && clientId
      ? `https://cdn.brandfetch.io/domain/${encodeURIComponent(domain)}/w/28/h/28/fallback/lettermark?c=${encodeURIComponent(clientId)}`
      : null;
  });

  toggleActions(): void {
    this.actionsOpen.update(open => !open);
  }

  closeActions(): void {
    this.actionsOpen.set(false);
  }

  @HostListener('document:click', ['$event'])
  closeOnOutsideClick(event: MouseEvent): void {
    if (!this.element.nativeElement.contains(event.target as Node)) this.closeActions();
  }

  @HostListener('document:keydown.escape')
  closeOnEscape(): void {
    this.closeActions();
  }
}
