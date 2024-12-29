import { fetchWithAuth } from '@/api';
import type { TaskRequest, TaskResponse } from '@/types/interfaces/task';

export const createTask = async (
    projectId: number,
    columnId: number,
    task: TaskRequest,
    token: string
): Promise<TaskResponse> => {
    return await fetchWithAuth<TaskResponse>(
        'POST',
        `/projects/${projectId}/columns/${columnId}/tasks`,
        token,
        task
    );
};

export const updateTask = async (
    projectId: number,
    columnId: number,
    taskId: number,
    updatedTask: Partial<TaskRequest>,
    token: string
): Promise<TaskResponse> => {
    return await fetchWithAuth<TaskResponse>(
        'PUT',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}`,
        token,
        updatedTask
    );
};

export const deleteTask = async (
    projectId: number,
    columnId: number,
    taskId: number,
    token: string
): Promise<void> => {
    return await fetchWithAuth<void>(
        'DELETE',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}`,
        token
    );
};

export const getTaskById = async (
    projectId: number,
    columnId: number,
    taskId: number,
    token: string
): Promise<TaskResponse> => {
    return await fetchWithAuth<TaskResponse>('GET', `/projects/${projectId}/columns/${columnId}/tasks/${taskId}`, token);
};