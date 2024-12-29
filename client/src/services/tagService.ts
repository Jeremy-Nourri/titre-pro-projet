import { fetchWithAuth } from '@/api';
import type { TagRequest, TagResponse } from '@/types/interfaces/tag';

export const createTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tag: TagRequest,
    token: string
): Promise<TagResponse> => {
    return await fetchWithAuth<TagResponse>(
        'POST',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags`,
        token,
        tag
    );
};

export const updateTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number,
    updatedTag: Partial<TagRequest>,
    token: string
): Promise<TagResponse> => {
    return await fetchWithAuth<TagResponse>(
        'PUT',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`,
        token,
        updatedTag
    );
};

export const deleteTag = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number,
    token: string
): Promise<void> => {
    return await fetchWithAuth<void>(
        'DELETE',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`,
        token
    );
};

export const getTagById = async (
    projectId: number,
    columnId: number,
    taskId: number,
    tagId: number,
    token: string
): Promise<TagResponse> => {
    return await fetchWithAuth<TagResponse>(
        'GET',
        `/projects/${projectId}/columns/${columnId}/tasks/${taskId}/tags/${tagId}`,
        token
    );
};
