import type { UserProjectResponse } from "./userProject";
import type { ProjectResponse } from "./project";

export interface UserRequest {
	firstName: string;
	lastName: string;
	email: string;
	password: string;
	position: string;
}

export interface UserResponse {
	id: number;
	firstName: string;
	lastName: string;
	email: string;
	position: string;
	createdAt: string;
	updatedAt?: string;
	createdProjects: ProjectResponse[];
	userProjects: UserProjectResponse[];
}
