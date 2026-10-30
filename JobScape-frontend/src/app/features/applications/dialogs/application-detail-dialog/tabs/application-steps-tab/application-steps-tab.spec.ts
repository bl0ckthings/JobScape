import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationStepsTab } from './application-steps-tab';
import { DetailStep } from '../../application-detail.models';

describe('ApplicationStepsTab', () => {
  let component: ApplicationStepsTab;
  let fixture: ComponentFixture<ApplicationStepsTab>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationStepsTab],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationStepsTab);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('uses each historical status for its title and accessible label', async () => {
    const steps: DetailStep[] = [
      { id: 1, status: 'APPLIED', date: '2026-01-08', comment: '', contactId: null },
      { id: 2, status: 'FIRST_CONTACT', date: '2026-01-12', comment: '', contactId: null },
    ];
    fixture.componentRef.setInput('steps', steps);
    await fixture.whenStable();

    const cards = [...fixture.nativeElement.querySelectorAll('.detail-card')] as HTMLElement[];
    expect(cards.map((card) => card.querySelector('h3')?.textContent?.trim())).toEqual([
      'CV envoyé',
      'Premier contact',
    ]);
    expect(cards[0].getAttribute('aria-label')).toBe('Modifier l’étape CV envoyé');
    expect(cards[1].getAttribute('aria-label')).toBe('Modifier l’étape Premier contact');
  });
});
