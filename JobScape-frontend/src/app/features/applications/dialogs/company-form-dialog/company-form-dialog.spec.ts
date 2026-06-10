import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyFormDialog } from './company-form-dialog';

describe('CompanyFormDialog', () => {
  let component: CompanyFormDialog;
  let fixture: ComponentFixture<CompanyFormDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompanyFormDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CompanyFormDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
