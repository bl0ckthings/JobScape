import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TimeoutError } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { BrandfetchSearchService } from './brandfetch-search.service';

describe('BrandfetchSearchService', () => {
  let http: HttpTestingController;
  let service: BrandfetchSearchService;
  let originalClientId: string;

  beforeEach(() => {
    originalClientId = environment.brandFetch_clientId;
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    service = TestBed.inject(BrandfetchSearchService);
  });

  afterEach(() => {
    http.verify();
    environment.brandFetch_clientId = originalClientId;
  });

  it('searches by encoded name with the public client ID', () => {
    environment.brandFetch_clientId = 'test-client-id';
    const results = [{ brandId: '1', name: 'ACME France', domain: 'acme.com', icon: 'https://example.com/icon' }];
    let received = null;

    service.search('ACME France').subscribe(value => received = value);

    const request = http.expectOne('https://api.brandfetch.io/v2/search/ACME%20France?c=test-client-id');
    expect(request.request.method).toBe('GET');
    expect(request.request.headers.has('Authorization')).toBe(false);
    request.flush(results);
    expect(received).toEqual(results);
  });

  it('does not call Brandfetch before the client ID is configured', () => {
    environment.brandFetch_clientId = '';
    let received = null;

    service.search('ACME').subscribe(value => received = value);

    http.expectNone(request => request.url.includes('brandfetch.io'));
    expect(received).toEqual([]);
  });

  it('times out when Brandfetch never responds', async () => {
    vi.useFakeTimers();
    try {
      let receivedError: unknown;
      service.search('ACME').subscribe({ error: error => receivedError = error });
      const request = http.expectOne(request => request.url.includes('/v2/search/ACME'));

      await vi.advanceTimersByTimeAsync(8000);

      expect(receivedError).toBeInstanceOf(TimeoutError);
      expect(request.cancelled).toBe(true);
    } finally {
      vi.useRealTimers();
    }
  });
});
