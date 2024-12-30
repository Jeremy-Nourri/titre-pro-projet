import { api } from '@/api';
import type { TagRequest, TagResponse } from '@/types/interfaces/tag';

export const createTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tag: TagRequest
): Promise<TagResponse> => {
    const response = await api.post<TagResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags`,
        tag
    );
    return response.data;
};

export const updateTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number,
    updatedTag: Partial<TagRequest>
): Promise<TagResponse> => {
    const response = await api.put<TagResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`,
        updatedTag
    );
    return response.data;
};

export const deleteTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number
): Promise<void> => {
    await api.delete<void>(`/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`);
};

export const getTagById = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number
): Promise<TagResponse> => {
    const response = await api.get<TagResponse>(
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`
    );
    return response.data;
};
