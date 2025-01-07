export interface UserProjectRequest {
	projectId: number;
    userEmail: string;
	role: Role;
}

export interface UserProjectResponse {
	id: number;
	projectName: string;
	projectDescription: string;
	projectId: number;
    userId: number;
	endDate: string;
	startDate: string
	role: Role;
	userAddedAt: string;
}

export enum Role {
	ADMIN = 'Administrateur',
    MEMBER = 'Membre'
}