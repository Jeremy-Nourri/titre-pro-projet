import { api } from '@/api';
import type { ProjectRequest, ProjectResponse } from '@/types/interfaces/project';
import type { UserProjectRequest } from '@/types/interfaces/userProject';

export const getProjectById = async (projectId: number): Promise<ProjectResponse> => {
    try {
        const response = await api.get<ProjectResponse>(`/projects/${projectId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const createProject = async (project: ProjectRequest): Promise<ProjectResponse> => {
    try {
        const response = await api.post<ProjectResponse>('/projects/create', project);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const deleteProject = async (projectId: number): Promise<void> => {
    try {
        await api.delete<void>(`/projects/${projectId}`);
    
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const addUserInProject = async(userProject: UserProjectRequest): Promise<string> => {
    try {
        const response = await api.post<string>(`/projects/${userProject.projectId}/adduser`, userProject);
        return response.data;

    } catch (error) {
        console.error(error);
        throw error;
    }
};
