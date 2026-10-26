import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationCard } from './application-card';

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
});
