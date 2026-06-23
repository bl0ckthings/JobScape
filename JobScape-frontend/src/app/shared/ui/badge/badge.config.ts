// shared/ui/badge/badge.config.ts

import { ApplicationStatus } from '../../models/application-status.model';



export const APPLICATION_STATUS_BADGE_CONFIG: Record<
  ApplicationStatus,
  {
    label: string;
    className: string;
  }
> = {
  WISHLIST: {
    label: 'À postuler',
    className: 'bg-[#2563EB]/20 border-[#2563EB]/35 text-[#1D4ED8]',
  },

  APPLIED: {
    label: 'CV envoyé',
    className: 'bg-[#22C55E]/20 border-[#22C55E]/30 text-[#0F5132]',
  },

  FOLLOWED_UP: {
    label: 'Relancé',
    className: 'bg-[#64748B]/15 border-[#64748B]/25 text-[#475569]',
  },

  FIRST_CONTACT: {
    label: 'Premier contact',
    className: 'bg-[#FF8D28]/20 border-[#FF8D28]/35 text-[#B45309]',
  },

  HR_INTERVIEW: {
    label: 'Entretien RH',
    className: 'bg-[#FBFF00]/20 border-[#AC7F5E]/35 text-[#854D0E]',
  },

  MANAGER_INTERVIEW: {
    label: 'Entretien Manager',
    className: 'bg-[#22D3EE]/20 border-[#22D3EE]/35 text-[#0E7490]',
  },

  TECHNICAL_TEST: {
    label: 'Test technique',
    className: 'bg-[#22EE33]/20 border-[#147E1C]/30 text-[#166534]',
  },

  OFFER_RECEIVED: {
    label: 'Offre reçue',
    className: 'bg-[#CB30E0]/20 border-[#CB30E0]/35 text-[#86198F]',
  },

  REJECTED: {
    label: 'Rejeté',
    className: 'bg-[#F8080D]/30 border-[#F8080D]/35 text-[#991B1B]',
  },
};
