import { ApplicationStatus } from '../../models/applications/application-status.model';



export const APPLICATION_STATUS_BADGE_CONFIG: Record<
  ApplicationStatus,
  {
    label: string;
    className: string;
  }
> = {
  WISHLIST: {
    label: 'À postuler',
    className: 'bg-[#2563EB]/20 border-[#2563EB]/35 text-[#0F5132]',
  },

  APPLIED: {
    label: 'CV envoyé',
    className: 'bg-[#23C483]/10 border-[#23C483]/35 text-[#0F5132]',
  },

  FOLLOWED_UP: {
    label: 'Relancé',
    className: 'bg-[#64748B]/20 border-[#64748B]/35 text-[#0F5132]',
  },

  FIRST_CONTACT: {
    label: 'Premier contact',
    className: 'bg-[#FF8D28]/20 border-[#FF8D28]/35 text-[#0F5132]',
  },

  HR_INTERVIEW: {
    label: 'Entretien RH',
    className: 'bg-[#FBFF00]/20 border-[#AC7F5E]/35 text-[#0F5132]',
  },

  MANAGER_INTERVIEW: {
    label: 'Entretien Manager',
    className: 'bg-[#22D3EE]/20 border-[#22D3EE]/35 text-[#0F5132]',
  },

  TECHNICAL_TEST: {
    label: 'Test technique',
    className: 'bg-[#22EE33]/20 border-[#147E1C]/30 text-[#0F5132]',
  },

  OFFER_RECEIVED: {
    label: 'Offre reçue',
    className: 'bg-[#CB30E0]/20 border-[#CB30E0]/35 text-[#0F5132]',
  },

  REJECTED: {
    label: 'Rejeté',
    className: 'bg-[#F8080D]/30 border-[#AC7F5E]/35 text-[#0F5132]',
  },
};
