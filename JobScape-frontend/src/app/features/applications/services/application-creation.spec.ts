import { TestBed } from '@angular/core/testing';

import { ApplicationCreation } from './application-creation';

describe('ApplicationCreation', () => {
  let service: ApplicationCreation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationCreation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
