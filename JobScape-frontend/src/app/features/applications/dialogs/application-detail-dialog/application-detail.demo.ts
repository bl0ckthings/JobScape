import { ApplicationDetailData } from './application-detail.models';

export function createApplicationDetailDemo(): ApplicationDetailData {
  return {
    companyName: 'Dassault',
    companyLogoUrl: null,
    jobTitle: 'Back-end Engineer',
    source: 'Réponse à offre',
    workMode: 'Remote',
    status: 'APPLIED',
    createdLabel: 'Créée il y a 2 j.',
    steps: [
      {
        id: 1,
        nature: 'Entretien RH',
        status: 'interview',
        date: '2026-01-08',
        comment: 'Bon échange, contact sous 7 jours.',
        contactId: 1,
      },
      {
        id: 2,
        nature: 'Test technique',
        status: 'todo',
        date: '2026-01-12',
        comment: 'Préparer Angular, Spring Boot et SQL.',
        contactId: null,
      },
    ],
    contacts: [
      {
        id: 1,
        name: 'Marie Blachère',
        role: 'Responsable RH',
        company: 'Dassault',
        email: 'marie.blachere@example.com',
        phone: '+33 6 12 34 56 78',
        primary: true,
      },
      {
        id: 2,
        name: 'Julien Aigredouce',
        role: 'Manager',
        company: 'Dassault',
        email: '',
        phone: '',
      },
    ],
    reminders: [
      {
        id: 1,
        title: 'Relancer après entretien',
        date: '2026-01-15',
        time: '10:00',
        comment: 'Préparer le message de relance.',
        completed: false,
      },
      {
        id: 2,
        title: 'Préparer entretien RH',
        date: '2026-01-07',
        time: '18:00',
        comment: 'Relire la fiche de poste.',
        completed: true,
      },
    ],
  };
}
