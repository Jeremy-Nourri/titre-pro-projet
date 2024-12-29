import { fetchWithAuth } from '@/api';
import type { BoardColumnRequest, BoardColumnResponse } from '@/types/interfaces/boardColumn';

export const createColumn = async (projectId: number, column: BoardColumnRequest, token: string): Promise<BoardColumnResponse> => {
    return await fetchWithAuth<BoardColumnResponse>('POST', `/projects/${projectId}/columns`, token, column);
};

export const updateColumn = async (projectId: number, columnId: number, data: Partial<BoardColumnRequest>, token: string): Promise<BoardColumnResponse> => {
    return await fetchWithAuth<BoardColumnResponse>('PUT', `/projects/${projectId}/columns/${columnId}`, token, data);
};

export const deleteColumn = async (projectId: number, columnId: number, token: string): Promise<void> => {
    return await fetchWithAuth<void>('DELETE', `/projects/${projectId}/columns/${columnId}`, token);
};

export const getColumnById = async (projectId: number, columnId: number, token: string): Promise<BoardColumnResponse> => {
    return await fetchWithAuth<BoardColumnResponse>('GET', `/projects/${projectId}/columns/${columnId}`, token);
};