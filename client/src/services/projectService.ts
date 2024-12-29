import { fetchWithAuth } from '@/api';
import type { ProjectRequest, ProjectResponse } from '@/types/interfaces/project';

export const getProjectById = async (projectId: number, token: string): Promise<ProjectResponse> => {
    return await fetchWithAuth<ProjectResponse>('GET', `/projects/${projectId}`, token);
};

export const createProject = async (project: ProjectRequest, token: string): Promise<ProjectResponse> => {
    return await fetchWithAuth<ProjectResponse>('POST', `/projects/create`, token, project);
};

export const deleteProject = async (projectId: number, token: string): Promise<void> => {
    return await fetchWithAuth<void>('DELETE', `/projects/${projectId}`, token);
};