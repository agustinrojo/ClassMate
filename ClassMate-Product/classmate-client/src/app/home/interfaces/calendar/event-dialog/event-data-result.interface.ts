export interface EventDataResult {
  delete?: boolean; // Indicates if the delete button was clicked
  eventId?: number; // ID of the event to be deleted
  title?: string;   // Updated title of the event
  description?: string;
  start?: string;   // Updated start date of the event
  end?: string;     // Updated end date of the event
}
