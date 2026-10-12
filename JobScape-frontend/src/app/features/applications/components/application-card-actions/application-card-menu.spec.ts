import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationCardMenu } from './application-card-menu';

describe('ApplicationCardMenu', () => {
  let component: ApplicationCardMenu;
  let fixture: ComponentFixture<ApplicationCardMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationCardMenu],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationCardMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
