import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationCard } from './application-card';
import { ApplicationDto } from '../../../../shared/models/applications/applications-by-status-response.model';

describe('ApplicationCard', () => {
  let component: ApplicationCard;
  let fixture: ComponentFixture<ApplicationCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('opens and closes the application actions in the card', () => {
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('[aria-label="Actions"]');

    button.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[data-testid="application-actions-menu"]')).toBeTruthy();

    button.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[data-testid="application-actions-menu"]')).toBeNull();
  });

  it('shows a company logo beside its name at text size', () => {
    fixture.componentRef.setInput('companyName', 'Acme');
    fixture.componentRef.setInput('companyDomain', 'acme.com');
    fixture.detectChanges();

    const heading = fixture.nativeElement.querySelector('[data-testid="company-heading"]') as HTMLElement;
    const logo = heading.querySelector('img') as HTMLImageElement;

    expect(heading.textContent).toContain('Acme');
    expect(logo.src).toContain('/domain/acme.com/');
    expect(logo.src).toContain('c=');
    expect(logo.className).toContain('h-[1em]');
    expect(logo.className).toContain('w-[1em]');
  });

  it('shows the initial instead of a broken logo when domain is missing', () => {
    fixture.componentRef.setInput('companyName', 'Acme');
    fixture.componentRef.setInput('companyDomain', null);
    fixture.detectChanges();

    const heading = fixture.nativeElement.querySelector('[data-testid="company-heading"]') as HTMLElement;
    expect(heading.querySelector('img')).toBeNull();
    expect(heading.textContent).toContain('A');
  });

  it('shows company name and stored logo for an application from the API', () => {
    const application: ApplicationDto = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'APPLIED', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, companyName: 'Acme', companyDomain: 'acme.com',
      companyLogoUrl: 'https://example.com/acme.png', steps: [],
    };
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const heading = fixture.nativeElement.querySelector('[data-testid="company-heading"]') as HTMLElement;
    expect(heading.textContent).toContain('Acme');
    expect(heading.textContent).toContain('Paris');
    expect((heading.querySelector('img') as HTMLImageElement).src).toBe(application.companyLogoUrl);
    expect(fixture.nativeElement.textContent).toContain('Developer');
  });

  it('uses the shared status badge on the application card', () => {
    const application: ApplicationDto = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'HR_INTERVIEW', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, companyName: 'Acme', companyDomain: 'acme.com',
      companyLogoUrl: null, steps: [],
    };
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const badge = fixture.nativeElement.querySelector('app-badge span') as HTMLSpanElement;
    expect(badge.textContent?.trim()).toBe('Entretien RH');
    expect(badge.classList.contains('bg-[#FBFF00]/20')).toBe(true);
  });

  it('uses the company initial when an API application has no logo or domain', () => {
    const application: ApplicationDto = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'APPLIED', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, companyName: 'Startup', companyDomain: null,
      companyLogoUrl: null, steps: [],
    };
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const heading = fixture.nativeElement.querySelector('[data-testid="company-heading"]') as HTMLElement;
    expect(heading.querySelector('img')).toBeNull();
    expect(heading.textContent).toContain('Startup');
    expect(heading.textContent).toContain('S');
  });

  it('uses the company domain when an API application has no stored logo', () => {
    const application: ApplicationDto = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'APPLIED', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, companyName: 'Acme', companyDomain: 'acme.com',
      companyLogoUrl: null, steps: [],
    };
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const logo = fixture.nativeElement.querySelector('[data-testid="company-heading"] img') as HTMLImageElement;
    expect(logo.src).toContain('/domain/acme.com/');
  });

  it('replaces an expired Brandfetch search icon with a fresh domain logo', () => {
    const application: ApplicationDto = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'APPLIED', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, companyName: 'Acme', companyDomain: 'acme.com',
      companyLogoUrl: 'https://cdn.brandfetch.io/brand-id/icon.webp?c=expired', steps: [],
    };
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const logo = fixture.nativeElement.querySelector('[data-testid="company-heading"] img') as HTMLImageElement;
    expect(logo.src).toContain('/domain/acme.com/');
    expect(logo.src).not.toContain('expired');
  });

  it('does not show a fake company when an older API response lacks company details', () => {
    const application = {
      id: 1, jobTitle: 'Developer', city: 'Paris', country: 'France',
      source: 'LinkedIn', url: null, status: 'APPLIED', workMode: 'Remote',
      createdAt: '2026-10-27T10:00:00', updatedAt: '2026-10-27T10:00:00',
      userId: 1, companyId: 2, steps: [],
    } as unknown as ApplicationDto;
    fixture.componentRef.setInput('application', application);
    fixture.detectChanges();

    const heading = fixture.nativeElement.querySelector('[data-testid="company-heading"]') as HTMLElement;
    expect(heading.textContent).toContain('Entreprise');
    expect(heading.textContent).not.toContain('Componize');
    expect(heading.querySelector('img')).toBeNull();
  });
});
