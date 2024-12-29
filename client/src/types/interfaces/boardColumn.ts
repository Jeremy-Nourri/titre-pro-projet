import type { TaskResponse } from "./task";

export interface BoardColumnRequest {
  name: string;
  projectId: number;
  tasks?: TaskResponse[];
}

export interface BoardColumnResponse {
	id: number;
  name: string;
  projectId: number;
  tasks: TaskResponse[];
}

