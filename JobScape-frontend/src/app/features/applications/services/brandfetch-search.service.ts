import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { environment } from '../../../../environments/environment';

export interface BrandfetchResult {
  brandId: string;
  name: string | null;
  domain: string;
  icon: string | null;
}

@Injectable({ providedIn: 'root' })
export class BrandfetchSearchService {
  private readonly http = inject(HttpClient);

  get enabled(): boolean {
    return !!environment.brandFetch_clientId.trim();
  }

  search(name: string): Observable<BrandfetchResult[]> {
    const query = name.trim();
    if (!this.enabled || !query) {
      return of([]);
    }

    return this.http.get<BrandfetchResult[]>(
      `https://api.brandfetch.io/v2/search/${encodeURIComponent(query)}`,
      { params: { c: environment.brandFetch_clientId } },
    );
  }
}
