import type { UserSimplifiedResponse } from "./userSimplified";

export interface ProjectRequest {
  name: string;
  description?: string;
  startDate: string;
  endDate: string;
  createdBy: number;
}

export interface ProjectResponse {
  id: number;
  name: string;
  description?: string;
  startDate: string;
  endDate: string;
  createdAt: string;
  updatedAt?: string;
  createdBy: UserSimplifiedResponse;
  users: UserSimplifiedResponse[];
}

