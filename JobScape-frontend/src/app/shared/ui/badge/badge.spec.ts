import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Badge } from './badge';
import { APPLICATION_STATUS_BADGE_CONFIG } from './badge.config';
import { ApplicationStatus } from '../../models/applications/application-status.model';

describe('Badge', () => {
  let component: Badge;
  let fixture: ComponentFixture<Badge>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Badge],
    }).compileComponents();

    fixture = TestBed.createComponent(Badge);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('status', 'APPLIED');
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([
    ['WISHLIST', 'bg-[#2563EB]/20 border-[#2563EB]/35 text-[#0F5132]'],
    ['APPLIED', 'bg-[#23C483]/10 border-[#23C483]/35 text-[#0F5132]'],
    ['FOLLOWED_UP', 'bg-[#64748B]/20 border-[#64748B]/35 text-[#0F5132]'],
    ['FIRST_CONTACT', 'bg-[#FF8D28]/20 border-[#FF8D28]/35 text-[#0F5132]'],
    ['HR_INTERVIEW', 'bg-[#FBFF00]/20 border-[#AC7F5E]/35 text-[#0F5132]'],
    ['MANAGER_INTERVIEW', 'bg-[#22D3EE]/20 border-[#22D3EE]/35 text-[#0F5132]'],
    ['TECHNICAL_TEST', 'bg-[#22EE33]/20 border-[#147E1C]/30 text-[#0F5132]'],
    ['OFFER_RECEIVED', 'bg-[#CB30E0]/20 border-[#CB30E0]/35 text-[#0F5132]'],
    ['REJECTED', 'bg-[#F8080D]/30 border-[#AC7F5E]/35 text-[#0F5132]'],
  ] as [ApplicationStatus, string][])('uses the Figma colors for %s', (status, classes) => {
    expect(APPLICATION_STATUS_BADGE_CONFIG[status].className).toBe(classes);
    fixture.componentRef.setInput('status', status);
    fixture.detectChanges();

    const badge = fixture.nativeElement.querySelector('span') as HTMLSpanElement;
    for (const className of classes.split(' ')) {
      expect(badge.classList.contains(className)).toBe(true);
    }
  });
});
