import type { BoardColumnResponse } from "./boardColumn";
import type { UserSimplifiedResponse } from "./userSimplified";

export interface Project {
  name: string;
  description?: string;
  startDate: string;
  endDate: string;
}

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
  createdDate: string;
  updatedDate?: string;
  createdBy: UserSimplifiedResponse;
  users: UserSimplifiedResponse[];
  columns: BoardColumnResponse[];
}

