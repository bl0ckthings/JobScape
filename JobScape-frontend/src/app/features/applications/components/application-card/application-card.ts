import { Component, computed, input } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { environment } from '../../../../../environments/environment';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';
import { APPLICATION_STATUS_BADGE_CONFIG } from '../../../../shared/ui/badge/badge.config';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-application-card',
  imports: [MatMenuModule, MatIconModule, MatButtonModule, DatePipe],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {
  readonly application = input<ApplicationDto | null>(null);
  readonly companyName = input('');
  readonly companyDomain = input<string | null>(null);
  readonly heading = computed(() => this.application()?.companyName?.trim() || this.companyName().trim() || 'Entreprise');
  readonly statusLabel = computed(() => {
    const application = this.application();
    return application ? APPLICATION_STATUS_BADGE_CONFIG[application.status].label : 'CV Envoyé';
  });

  readonly logoUrl = computed(() => {
    const application = this.application();
    const storedLogo = application?.companyLogoUrl?.trim();
    if (storedLogo) return storedLogo;

    const domain = (application ? application.companyDomain : this.companyDomain())?.trim();
    const clientId = environment.brandFetch_clientId.trim();

    return domain && clientId
      ? `https://cdn.brandfetch.io/domain/${encodeURIComponent(domain)}/w/28/h/28/fallback/lettermark?c=${encodeURIComponent(clientId)}`
      : null;
  });
}
