import { api, handleApiError } from '@/api';

import type { ProjectRequest, ProjectResponse } from '@/types/interfaces/project';


export const createProject = async (project: ProjectRequest, token: string): Promise<ProjectResponse | string> => {
    try {
        const response = await api.post('projects/create', project, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
        });
        
        return response.data;
        
    } catch (error) {
        return handleApiError(error);
    }
};

export const getProjectById = async (id: number, token: string): Promise<ProjectResponse | string> => {
    try {
        const response = await api.get(`projects/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
        });
        
        return response.data;
        
    } catch (error) {
        return handleApiError(error);
    }
};