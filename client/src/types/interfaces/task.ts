import type { TagResponse } from "./tag";

export interface TaskRequest {
  title: string;
  dueDate: string;
  detail?: string;
  priority: Priority,
  taskStatus: Status,
  columnBoardId?: number;
  tags?: TagResponse[];
  createdAt?: string;
  updatedAt?: string;
}

export interface TaskResponse {
  id: number;
  title: string;
  dueDate: string;
  detail?: string;
  priority: Priority;
  taskStatus: Status;
  columnBoardId: number;
  tags: TagResponse[];
  createdAt: string;
  updatedAt?: string;
}

export interface TaskSimplifiedResponse {
  id: number;
  title: string;
  priority: Status
}

export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
}
export enum Status {
  NOT_STARTED = 'NOT_STARTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
}
