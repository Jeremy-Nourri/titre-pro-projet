import { api } from '@/api';
import type { BoardColumnRequest, BoardColumnResponse } from '@/types/interfaces/boardColumn';
import type { ProjectResponse } from '@/types/interfaces/project';

export const createColumn = async (projectId: number, column: BoardColumnRequest): Promise<BoardColumnResponse> => {
    try {
        
    } catch (error) {
        console.error(error);
        throw error;
    }
    const response = await api.post<BoardColumnResponse>(`/projects/${projectId}/columns`, column);
    return response.data;
};

export const updateColumn = async (projectId: number, columnId: number, data: Partial<BoardColumnRequest>): Promise<BoardColumnResponse> => {
    try {
        const response = await api.put<BoardColumnResponse>(`/projects/${projectId}/columns/${columnId}`, data);
        return response.data;

    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const deleteColumn = async (projectId: number, columnId: number): Promise<ProjectResponse> => {
    try {
        const response = await api.delete<ProjectResponse>(`/projects/${projectId}/columns/${columnId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const getColumnById = async (projectId: number, columnId: number): Promise<BoardColumnResponse> => {
    try {
        const response = await api.get<BoardColumnResponse>(`/projects/${projectId}/columns/${columnId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};
