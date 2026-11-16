export type NotificationType = 'REMINDER_DUE' | 'INTERVIEW_SOON' | 'APPLICATION_UPDATE';

export interface JobNotification {
  id: string;
  type: NotificationType;
  message: string;
  createdAt: string;
  readAt: string | null;
  applicationId: number | null;
  reminderId: number | null;
}
