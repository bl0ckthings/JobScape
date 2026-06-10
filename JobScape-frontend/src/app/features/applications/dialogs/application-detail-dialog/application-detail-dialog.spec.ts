import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationDetailDialog } from './application-detail-dialog';

describe('ApplicationDetailDialog', () => {
  let component: ApplicationDetailDialog;
  let fixture: ComponentFixture<ApplicationDetailDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationDetailDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationDetailDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
