import { defineStore } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";

import type { ProjectResponse, ProjectRequest } from "@/types/interfaces/project";
import type { BoardColumnRequest } from "@/types/interfaces/boardColumn";
import type { TaskRequest } from "@/types/interfaces/task";

import { getProjectById, createProject } from "@/services/projectService";
// import { createTag, updateTag } from "@/services/tagService";
import {
    createColumn,
    updateColumn,
    deleteColumn,
} from "@/services/boardColumnService";
import {
    createTask,
    updateTask,
    deleteTask,
} from "@/services/taskService";

export const useProjectStore = defineStore("project", () => {
    const router = useRouter();

    const projectState = ref<ProjectResponse | null>(null);
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const resetError = () => {
        error.value = null;
    };

    const handleError = (err: unknown): string => {
        if (err instanceof Error) {
            return err.message;
        }
        return "Une erreur inattendue s'est produite.";
    };

    const getProjectId = (): number => {
        if (!projectState.value?.id) {
            throw new Error("Project ID est introuvable.");
        }
        return projectState.value.id;
    };

    const addProject = async (project: ProjectRequest) => {
        isLoading.value = true;
        resetError();
        try {
            const response = await createProject(project);
            projectState.value = response;
            router.push({
                name: "ProjectBoardView",
                params: { id: projectState.value?.id },
            });
            return response;
        } catch (err) {
            error.value = handleError(err);
        } finally {
            isLoading.value = false;
        }
    };

    const fetchProjectById = async (projectId: number) => {
        isLoading.value = true;
        resetError();
        try {
            const response = await getProjectById(projectId);
            projectState.value = response;
            return response;
        } catch (err) {
            error.value = handleError(err);
        } finally {
            isLoading.value = false;
        }
    };

    const addColumn = async (column: BoardColumnRequest) => {
        try {
            const response = await createColumn(column.projectId, column);
            projectState.value?.boardColumns.push(response);
            await fetchProjectById(column.projectId);
        } catch (err) {
            error.value = handleError(err);
        }
    };

    const updateColumnName = async (columnId: number, name: string) => {
        try {
            const projectId = getProjectId();
            await updateColumn(projectId, columnId, { name });
            const column = projectState.value?.boardColumns.find((col) => col.id === columnId);
            if (column) column.name = name;
        } catch (err) {
            error.value = handleError(err);
        }
    };

    const removeColumn = async (columnId: number) => {
        try {
            const projectId = getProjectId();
            const response = await deleteColumn(projectId, columnId);
            projectState.value = response;
        } catch (err) {
            error.value = handleError(err);
        }
    };

    const addTask = async (columnId: number, task: TaskRequest) => {
        try {
            const projectId = getProjectId();
            const response = await createTask(projectId, columnId, task);
            const column = projectState.value?.boardColumns.find((col) => col.id === columnId);
            if (column) column.tasks.push(response);
        } catch (err) {
            error.value = handleError(err);
        }
    };

    const updateTaskDetails = async (
        oldColumnId: number,
        taskId: number,
        updatedTask: Partial<TaskRequest>
    ): Promise<void> => {
        try {
            const projectId = getProjectId();
    
            const newColumnId = updatedTask.columnBoardId ?? oldColumnId;
    
            const updatedTaskResponse = await updateTask(projectId, newColumnId, taskId, updatedTask);
    
            if (newColumnId !== oldColumnId) {

                const oldColumn = projectState.value?.boardColumns.find(
                    (col) => col.id === oldColumnId
                );
                const newColumn = projectState.value?.boardColumns.find(
                    (col) => col.id === newColumnId
                );
    
                if (oldColumn) {
                    oldColumn.tasks = oldColumn.tasks.filter((t) => t.id !== taskId);
                }
    
                if (newColumn) {
                    newColumn.tasks.push(updatedTaskResponse);
                }
            } else {

                const column = projectState.value?.boardColumns.find(
                    (col) => col.id === oldColumnId
                );
                const task = column?.tasks.find((t) => t.id === taskId);
                if (task) {
                    Object.assign(task, updatedTaskResponse);
                }
            }
        } catch (err) {
            error.value = handleError(err);
            console.error('Erreur dans updateTaskDetails:', err);
        }
    };

    const removeTask = async (columnId: number, taskId: number) => {
        try {
            const projectId = getProjectId();
            await deleteTask(projectId, columnId, taskId);
            const column = projectState.value?.boardColumns.find((col) => col.id === columnId);
            if (column) column.tasks = column.tasks.filter((task) => task.id !== taskId);
        } catch (err) {
            error.value = handleError(err);
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
    };
});