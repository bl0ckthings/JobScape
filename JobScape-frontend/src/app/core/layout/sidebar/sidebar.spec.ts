import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Sidebar } from './sidebar';
import { provideRouter } from '@angular/router';
import { ApplicationCreation } from '../../../features/applications/services/application-creation';

describe('Sidebar', () => {
  let component: Sidebar;
  let fixture: ComponentFixture<Sidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Sidebar],
      providers: [provideRouter([]), { provide: ApplicationCreation, useValue: { openCreateApplicationDialog: vi.fn() } }],
    }).compileComponents();

    fixture = TestBed.createComponent(Sidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('closes the mobile menu when Candidatures is selected', () => {
    const menuButton: HTMLButtonElement = fixture.nativeElement.querySelector('header button');
    menuButton.click();
    fixture.detectChanges();

    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href="/applications"]');
    link.click();

    expect(component.isOpen).toBe(false);
  });
});
