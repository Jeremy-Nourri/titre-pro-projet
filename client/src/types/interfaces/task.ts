import type { TagResponse } from "./tag";

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
  taskStatus: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED',
  columnnBoardId: number;
  tags: TagResponse[];
  createdAt: string;
  updatedAt?: string;
}

export interface TaskSimplifiedResponse {
  id: number;
  title: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
}
