import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationsHeader } from './applications-header';

describe('ApplicationsHeader', () => {
  let component: ApplicationsHeader;
  let fixture: ComponentFixture<ApplicationsHeader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationsHeader],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationsHeader);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('opens notifications and closes them again', () => {
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('[aria-label="Notifications"]');

    button.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[data-testid="notifications-panel"]')).toBeTruthy();

    button.click();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('[data-testid="notifications-panel"]')).toBeNull();
  });

  it('opens sort choices and keeps the selected choice visible', () => {
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('[aria-label="Trier les candidatures"]');

    button.click();
    fixture.detectChanges();
    const oldest: HTMLButtonElement = fixture.nativeElement.querySelector('[data-testid="sort-oldest"]');
    oldest.click();
    fixture.detectChanges();

    expect(button.textContent).toContain('Plus anciennes');
    expect(fixture.nativeElement.querySelector('[data-testid="sort-menu"]')).toBeNull();
  });
});
