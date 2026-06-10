import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationContactsTab } from './application-contacts-tab';

describe('ApplicationContactsTab', () => {
  let component: ApplicationContactsTab;
  let fixture: ComponentFixture<ApplicationContactsTab>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationContactsTab],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationContactsTab);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
