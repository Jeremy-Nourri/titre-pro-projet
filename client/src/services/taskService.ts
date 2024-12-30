import { api } from '@/api';
import type { TaskRequest, TaskResponse } from '@/types/interfaces/task';

export const createTask = async (
    projectId: number,
    columnId: number,
    task: TaskRequest
): Promise<TaskResponse> => {
    const response = await api.post<TaskResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks`,
        task
    );
    return response.data;
};

export const updateTask = async (
    projectId: number,
    columnId: number,
    taskId: number,
    updatedTask: Partial<TaskRequest>
): Promise<TaskResponse> => {
    const response = await api.put<TaskResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}`,
        updatedTask
    );
    return response.data;
};

export const deleteTask = async (
    projectId: number,
    columnId: number,
    taskId: number
): Promise<void> => {
    await api.delete<void>(`/projects/${projectId}/columns/${columnId}/tasks/${taskId}`);
};

export const getTaskById = async (
    projectId: number,
    columnId: number,
    taskId: number
): Promise<TaskResponse> => {
    const response = await api.get<TaskResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}`
    );
    return response.data;
};
