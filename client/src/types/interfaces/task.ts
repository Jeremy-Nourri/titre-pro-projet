export interface TaskRequest {
  title: string;
  dueDate: string;
  detail?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
}

export interface TaskResponse {
  id: number;
  title: string;
  dueDate: string;
  detail?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  createdAt: string;
  updatedAt?: string;
}
