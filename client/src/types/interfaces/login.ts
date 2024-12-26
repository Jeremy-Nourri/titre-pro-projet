import type { ProjectResponse } from "./project";
import type { UserProjectResponse } from "./userProject";

export interface LoginRequest {
 email: string;
 password: string;
}

export interface LoginResponse {
	id: number;
	firstName?: string;
	lastName?: string;
	email: string;
	position?: string;
	createdAt?: string;
	updatedAt?: string;
	token: string;
	createdProjects?: ProjectResponse[];
	userProjects?: UserProjectResponse[];
}