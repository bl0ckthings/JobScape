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
});
