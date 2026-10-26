import { Component, computed, input } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { environment } from '../../../../../environments/environment';
@Component({
  selector: 'app-application-card',
  imports: [MatMenuModule, MatIconModule,MatButtonModule],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {
  readonly companyName = input('Componize');
  readonly companyDomain = input<string | null>('componize.com');

  readonly logoUrl = computed(() => {
    const domain = this.companyDomain()?.trim();
    const clientId = environment.brandFetch_clientId.trim();

    return domain && clientId
      ? `https://cdn.brandfetch.io/domain/${encodeURIComponent(domain)}/w/28/h/28/fallback/lettermark?c=${encodeURIComponent(clientId)}`
      : null;
  });
}
