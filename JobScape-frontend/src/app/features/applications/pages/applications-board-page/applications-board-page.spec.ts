import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { ApplicationsBoardPage } from './applications-board-page';
import { ApplicationService } from '../../services/application.service';
import { ApplicationDto, ApplicationsByStatusResponse } from '../../../../shared/models/applications/applications-by-status-response.model';
import { ApplicationCreation } from '../../services/application-creation';

describe('ApplicationsBoardPage', () => {
  let component: ApplicationsBoardPage;
  let fixture: ComponentFixture<ApplicationsBoardPage>;
  const openCreateApplicationDialog = vi.fn();
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
      }, {
        provide: ApplicationCreation,
        useValue: { created$: of(), openCreateApplicationDialog },
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

  it('uses the Figma colors for the Envoyé and Entretien column dots', () => {
    const columns = fixture.nativeElement.querySelectorAll('app-applications-status-column');

    expect(columns[1].querySelector('[data-testid="status-dot"]').classList.contains('bg-[#6D5EFC]')).toBe(true);
    expect(columns[2].querySelector('[data-testid="status-dot"]').classList.contains('bg-[#23C483]')).toBe(true);
  });

  it('opens the creation flow from the mobile add button', () => {
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('[data-testid="mobile-add-application"]');

    button.click();

    expect(openCreateApplicationDialog).toHaveBeenCalledOnce();
  });
});
