export interface ActivityResponseDTO {
  timeBucket: string; // ISO DateTime (e.g., "2025-02-08T00:00:00")
  count: number; // Aggregated activity count
}