import { api } from '@/api';
import type { ProjectRequest, ProjectResponse } from '@/types/interfaces/project';

export const getProjectById = async (projectId: number): Promise<ProjectResponse> => {
    const response = await api.get<ProjectResponse>(`/projects/${projectId}`);
    return response.data;
};

export const createProject = async (project: ProjectRequest): Promise<ProjectResponse> => {
    const response = await api.post<ProjectResponse>('/projects/create', project);
    return response.data;
};

export const deleteProject = async (projectId: number): Promise<void> => {
    await api.delete<void>(`/projects/${projectId}`);
};
