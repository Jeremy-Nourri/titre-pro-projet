import { defineStore } from 'pinia';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

import type { ProjectResponse, ProjectRequest } from '@/types/interfaces/project';
import type { BoardColumnRequest } from '@/types/interfaces/boardColumn';
import type { TaskRequest } from '@/types/interfaces/task';
import type { TagRequest } from '@/types/interfaces/tag';

import { getProjectById, createProject } from '@/services/projectService';
import { createColumn, updateColumn, deleteColumn } from '@/services/boardColumnService';
import { createTask, updateTask, deleteTask } from '@/services/taskService';
import { createTag, updateTag, deleteTag } from '@/services/tagService';

import { useAuthStore } from './authStore';

export const useProjectStore = defineStore('project', () => {
    const router = useRouter();
    const authStore = useAuthStore();

    const projectState = ref<ProjectResponse | null>(null);
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const resetError = () => {
        error.value = null;
    };

    const getProjectId = () => {
        if (!projectState.value?.id) {
            throw new Error('Project ID is introuvable.');
        }
        return projectState.value.id;
    };
    

    const addProject = async (project: ProjectRequest, token: string) => {
        isLoading.value = true;
        resetError();
        authStore.checkTokenInStore();

        const response = await createProject(project, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            projectState.value = response;
            router.push({
                name: 'ProjectBoardView',
                params: { id: projectState.value?.id },
            });
        }
        isLoading.value = false;
        return response;
    };

    const fetchProjectById = async (projectId: number, token: string) => {
        isLoading.value = true;
        resetError();
        const response = await getProjectById(projectId, token);

        if (typeof response === 'string') {
            error.value = response;
        } else {
            projectState.value = response;
        }

        isLoading.value = false;
        return response;
    };

    const addColumn = async (column: BoardColumnRequest, token: string) => {
        if (!projectState.value) return;
    
        const response = await createColumn(column.projectId, column, token);
        console.log('Nouvelle colonne créée :', response);
    
        if (typeof response === 'string') {
            error.value = response;
        } else {

            if (!projectState.value.boardColumns) {
                projectState.value.boardColumns = [];
            }
            projectState.value.boardColumns = [...projectState.value.boardColumns, response];
    
            await fetchProjectById(column.projectId, token);
        }
    };
    
    const updateColumnName = async (columnId: number, name: string, token: string) => {
        const projectId = getProjectId();
        const response = await updateColumn(projectId, columnId, { name }, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            const column = projectState.value?.boardColumns.find(col => col.id === columnId);
            if (column) column.name = name;
        }
    };
    
    const removeColumn = async (columnId: number, token: string) => {
        const projectId = getProjectId();
        const response = await deleteColumn(projectId, columnId, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            projectState.value!.boardColumns = projectState.value!.boardColumns.filter(col => col.id !== columnId);
        }
    };
    
    const addTask = async (columnId: number, task: TaskRequest, token: string) => {
        const projectId = getProjectId();
        const response = await createTask(projectId, columnId, task, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            const column = projectState.value?.boardColumns.find(col => col.id === columnId);
            if (column) column.tasks.push(response);
        }
    };
    
    const updateTaskDetails = async (
        columnId: number,
        taskId: number,
        updatedTask: Partial<TaskRequest>,
        token: string
    ) => {
        const projectId = getProjectId();
        const response = await updateTask(projectId, columnId, taskId, updatedTask, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            projectState.value?.boardColumns.forEach(col => {
                const task = col.tasks.find(t => t.id === taskId);
                if (task) Object.assign(task, response);
            });
        }
    };
    
    const removeTask = async (columnId: number, taskId: number, token: string) => {
        const projectId = getProjectId();
        console.log("entering removeTask")
        await deleteTask(projectId, columnId, taskId, token);

        projectState.value?.boardColumns.forEach((col) => {
            const index = col.tasks.findIndex((t) => t.id === taskId);
            if (index !== -1) {
                col.tasks.splice(index, 1);
            }
        })
        console.log("deleted tasks")
        
    };
    
    
    const addTag = async (
        columnId: number,
        taskId: number,
        tag: TagRequest,
        token: string
    ) => {
        const projectId = getProjectId();
        const response = await createTag(projectId, columnId, taskId, tag, token);
    
        if (typeof response === 'string') {
            error.value = response;
        } else {
            const column = projectState.value?.boardColumns.find(col => col.id === columnId);
            const task = column?.tasks.find(t => t.id === taskId);
    
            if (task) task.tags.push(response);
        }
    };
    
    
    const updateTagDetails = async (
        columnId: number,
        taskId: number,
        tagId: number,
        updatedTag: Partial<TagRequest>,
        token: string
    ) => {
        const projectId = getProjectId();
        const response = await updateTag(projectId, columnId, taskId, tagId, updatedTag, token);
    
        if (typeof response === 'string') {
            error.value = response;
        } else {
            const column = projectState.value?.boardColumns.find(col => col.id === columnId);
            const task = column?.tasks.find(t => t.id === taskId);
            const tag = task?.tags.find(t => t.id === tagId);
    
            if (tag) Object.assign(tag, response);
        }
    };
    
    
    const removeTag = async (
        columnId: number,
        taskId: number,
        tagId: number,
        token: string
    ) => {
        const projectId = getProjectId();
        const response = await deleteTag(projectId, columnId, taskId, tagId, token);
    
        if (typeof response === 'string') {
            error.value = response;
        } else {
            const column = projectState.value?.boardColumns.find(col => col.id === columnId);
            const task = column?.tasks.find(t => t.id === taskId);
    
            if (task) task.tags = task.tags.filter(t => t.id !== tagId);
        }
    };    
    
    const moveTaskToColumn = async (
        taskId: number,
        sourceColumnId: number,
        destinationColumnId: number,
        token: string
    ) => {
        const projectId = getProjectId();
        const sourceColumn = projectState.value?.boardColumns.find(col => col.id === sourceColumnId);
        const destinationColumn = projectState.value?.boardColumns.find(col => col.id === destinationColumnId);
    
        if (sourceColumn && destinationColumn) {
            const taskIndex = sourceColumn.tasks.findIndex(task => task.id === taskId);
    
            if (taskIndex !== -1) {
                
                const [movedTask] = sourceColumn.tasks.splice(taskIndex, 1);
                movedTask.columnBoardId = destinationColumnId;
    
                const response = await updateTask(
                    projectId,
                    sourceColumnId,
                    taskId,
                    { columnBoardId: destinationColumnId },
                    token
                );
    
                if (typeof response === 'string') {
                    error.value = response;
                    sourceColumn.tasks.splice(taskIndex, 0, movedTask);
                } else {
                    destinationColumn.tasks.push(response);
                }
            }
        }
    };

    return {
        projectState,
        isLoading,
        error,
        addProject,
        fetchProjectById,
        addColumn,
        updateColumnName,
        removeColumn,
        addTask,
        updateTaskDetails,
        removeTask,
        addTag,
        updateTagDetails,
        removeTag,
        moveTaskToColumn,
    };
});
