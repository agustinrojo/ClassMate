export interface EventDataResult {
  delete?: boolean; // Indicates if the delete button was clicked
  eventId?: number; // ID of the eventEntity to be deleted
  title?: string;   // Updated title of the eventEntity
  description?: string;
  start?: string;   // Updated start date of the eventEntity
  end?: string;     // Updated end date of the eventEntity
  edit?: boolean; // Indicates if the edit button was clicked
}
