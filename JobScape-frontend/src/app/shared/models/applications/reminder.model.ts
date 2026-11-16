export interface Reminder {
  id:number;
  applicationId: number;
  comment:string | null;
  reminderDate:string;
  completed: boolean;
}

export interface ReminderRequest {
  applicationId: number;
  comment: string | null;
  reminderDate: string;
  completed: boolean;
}
