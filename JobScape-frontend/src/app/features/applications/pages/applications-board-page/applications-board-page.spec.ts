import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { ApplicationsBoardPage } from './applications-board-page';
import { ApplicationService } from '../../services/application.service';
import { ApplicationDto, ApplicationsByStatusResponse } from '../../../../shared/models/applications/applications-by-status-response.model';

describe('ApplicationsBoardPage', () => {
  let component: ApplicationsBoardPage;
  let fixture: ComponentFixture<ApplicationsBoardPage>;
  const application = (id: number, jobTitle: string): ApplicationDto => ({
    id, jobTitle, city: 'Paris', country: 'France', source: 'LinkedIn', url: null,
    status: 'APPLIED', workMode: 'Remote', createdAt: '2026-10-27T10:00:00',
    updatedAt: '2026-10-27T10:00:00', userId: 1, companyId: 2,
    companyName: 'Acme', companyDomain: 'acme.com', companyLogoUrl: null, steps: [],
  });
  const response: ApplicationsByStatusResponse = {
    applicationsByStatus: {
      WISHLIST: [application(1, 'Wishlist role')],
      APPLIED: [application(2, 'Applied role')],
      INTERVIEW: [application(3, 'Interview role')],
      OFFER: [application(4, 'Offer role')],
      REJECTED: [],
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationsBoardPage],
      providers: [{
        provide: ApplicationService,
        useValue: { getApplicationByUserId: vi.fn(() => of(response)) },
      }],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationsBoardPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('shows three distinct columns populated from the current user applications', () => {
    const columns = fixture.nativeElement.querySelectorAll('app-applications-status-column');

    expect(columns).toHaveLength(3);
    expect(columns[0].textContent).toContain('Wishlist');
    expect(columns[0].textContent).toContain('Wishlist role');
    expect(columns[0].querySelectorAll('app-application-card')).toHaveLength(1);
    expect(columns[1].textContent).toContain('Envoyé');
    expect(columns[1].textContent).toContain('Applied role');
    expect(columns[2].textContent).toContain('Entretien');
    expect(columns[2].textContent).toContain('Interview role');
    expect(fixture.nativeElement.textContent).not.toContain('Offer role');
    expect(fixture.nativeElement.textContent).not.toContain('Componize');
  });
});
