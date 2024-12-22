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
	position: Position;
	createdAt: string;
	updatedAt?: string;
	createdProjects: ProjectResponse[];
	userProjects: UserProjectResponse[];
}

export enum Position {
  DEVELOPER = 'Développeur',
  PROJECT_MANAGER = 'Chef de projet',
  DESIGNER = 'Designer',
  TESTER = 'Testeur',
  DEVOPS = 'DevOps',
  BUSINESS_ANALYST = 'Analyste métier',
  ARCHITECT = 'Architecte',
  MARKETING = 'Marketing',
  PRODUCT_OWNER = 'Product Owner',
  SCRUM_MASTER = 'Scrum Master',
  TECHNICAL_LEAD = 'Lead Technique',
  CEO = 'CEO',
  CTO = 'CTO',
  CFO = 'CFO',
  HR_MANAGER = 'Responsable RH',
  COMMUNICATIONS_MANAGER = 'Responsable Communication',
}
