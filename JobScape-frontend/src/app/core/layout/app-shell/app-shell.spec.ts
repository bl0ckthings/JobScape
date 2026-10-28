import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppShell } from './app-shell';
import { provideRouter } from '@angular/router';
import { ApplicationCreation } from '../../../features/applications/services/application-creation';

describe('AppShell', () => {
  let component: AppShell;
  let fixture: ComponentFixture<AppShell>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppShell],
      providers: [provideRouter([]), { provide: ApplicationCreation, useValue: { openCreateApplicationDialog: vi.fn() } }],
    }).compileComponents();

    fixture = TestBed.createComponent(AppShell);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
