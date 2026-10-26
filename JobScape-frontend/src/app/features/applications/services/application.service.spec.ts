import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../../environments/environment';
import { CreateApplication } from '../../../shared/models/requests/create-application';
import { ApplicationService } from './application.service';

describe('ApplicationService', () => {
  let service: ApplicationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ApplicationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('posts new applications to the gateway application route', () => {
    const application: CreateApplication = {
      companyName: 'Acme',
      jobTitle: 'Developer',
      city: 'Paris',
      country: 'France',
      source: 'LinkedIn',
      workMode: 'Remote',
      initialStatus: 'APPLIED',
      initialDate: null,
      createReminder: false,
    };

    service.createNewApplication(application).subscribe();

    const request = http.expectOne(`${environment.apiBaseUrl}/api/application/create`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(application);
    request.flush({});
  });

  it('gets current user applications from the gateway application route', () => {
    service.getApplicationByUserId().subscribe();

    const request = http.expectOne(`${environment.apiBaseUrl}/api/application/user`);
    expect(request.request.method).toBe('GET');
    request.flush({ applicationsByStatus: {} });
  });
});
