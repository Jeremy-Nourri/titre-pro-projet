import { api } from '@/api';
import type { BoardColumnRequest, BoardColumnResponse } from '@/types/interfaces/boardColumn';
import type { ProjectResponse } from '@/types/interfaces/project';

export const createColumn = async (projectId: number, column: BoardColumnRequest): Promise<BoardColumnResponse> => {
    const response = await api.post<BoardColumnResponse>(`/projects/${projectId}/columns`, column);
    return response.data;
};

export const updateColumn = async (projectId: number, columnId: number, data: Partial<BoardColumnRequest>): Promise<BoardColumnResponse> => {
    const response = await api.put<BoardColumnResponse>(`/projects/${projectId}/columns/${columnId}`, data);
    return response.data;
};

export const deleteColumn = async (projectId: number, columnId: number): Promise<ProjectResponse> => {
    const response = await api.delete<ProjectResponse>(`/projects/${projectId}/columns/${columnId}`);
    return response.data;
};

export const getColumnById = async (projectId: number, columnId: number): Promise<BoardColumnResponse> => {
    const response = await api.get<BoardColumnResponse>(`/projects/${projectId}/columns/${columnId}`);
    return response.data;
};
