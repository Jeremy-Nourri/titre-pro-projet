import type { TaskSimplifiedResponse } from "./task";

export interface BoardColumnRequest {
  name: string;
}

export interface BoardColumnResponse {
	id: number;
    name: string;
    projectId: number;
    tasks: TaskSimplifiedResponse[];
}