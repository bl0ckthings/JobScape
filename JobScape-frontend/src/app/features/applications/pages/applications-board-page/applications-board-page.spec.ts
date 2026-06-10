import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationsBoardPage } from './applications-board-page';

describe('ApplicationsBoardPage', () => {
  let component: ApplicationsBoardPage;
  let fixture: ComponentFixture<ApplicationsBoardPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationsBoardPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ApplicationsBoardPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
