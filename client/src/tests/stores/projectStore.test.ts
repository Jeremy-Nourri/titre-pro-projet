import { setActivePinia, createPinia } from 'pinia';
import { useProjectStore } from '@/stores/projectStore';

import * as projectService from '@/services/projectService';
import * as boardColumnService from '@/services/boardColumnService';
import * as taskService from '@/services/taskService';

import { handleError } from '@/utils/handleError';
import { describe, it, expect, beforeEach, vi } from 'vitest';

vi.mock('@/services/projectService', () => ({
    createProject: vi.fn(),
    getProjectById: vi.fn(),
    addUserInProject: vi.fn(),
}));

vi.mock('@/services/boardColumnService', () => ({
    createColumn: vi.fn(),
    updateColumn: vi.fn(),
    deleteColumn: vi.fn(),
}));

vi.mock('@/services/taskService', () => ({
    createTask: vi.fn(),
    updateTask: vi.fn(),
    deleteTask: vi.fn(),
}));

vi.mock('@/utils/handleError');
vi.mock('vue-router', () => ({
    useRouter: vi.fn(() => ({ push: vi.fn() })),
}));

import type { ProjectResponse } from '@/types/interfaces/project';
import type { BoardColumnResponse } from '@/types/interfaces/boardColumn';
import type { TaskResponse, Priority, Status } from '@/types/interfaces/task';
import type { UserProjectResponse, Role, UserProjectRequest } from '@/types/interfaces/userProject';


const mockProject: ProjectResponse = {
    id: 1,
    name: 'Test Project',
    description: 'Description du projet',
    startDate: '2025-01-01',
    endDate: '2025-12-31',
    createdDate: '2025-01-01',
    createdBy: {
        id: 99,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        position: 'Développeur',
    },
    updatedDate: undefined,
    users: [],
    boardColumns: [],
};


const mockColumn: BoardColumnResponse = {
    id: 1,
    name: 'New Column',
    projectId: 1,
    tasks: [],
};

const mockTask: TaskResponse = {
    id: 1,
    title: 'Task 1',
    dueDate: '2025-02-01',
    detail: 'Description de la tâche',
    priority: 'LOW' as Priority,
    taskStatus: 'NOT_STARTED' as Status,
    columnBoardId: 1,
    tag: 'tag-test',
    tagColor: '#FF0000',
    createdAt: '2025-01-15',
    updatedAt: undefined,
};

const mockUserResponse: UserProjectResponse = {
    id: 1,
    projectName: 'Test Project',
    projectDescription: 'Description du projet',
    projectId: 1,
    userId: 2,
    startDate: '2025-01-01',
    endDate: '2025-12-31',
    role: 'ADMIN' as Role,
    userAddedAt: '2025-01-16',
};

const mockUserRequest: UserProjectRequest = {
    projectId: 1,
    userEmail: 'test.user@example.com',
    role: 'ADMIN' as Role,
};

describe('Project Store', () => {
    let store: ReturnType<typeof useProjectStore>;

    const mockedProjectService = vi.mocked(projectService);
    const mockedBoardColumnService = vi.mocked(boardColumnService);
    const mockedTaskService = vi.mocked(taskService);
    const mockedHandleError = vi.mocked(handleError);

    beforeEach(() => {
        setActivePinia(createPinia());
        store = useProjectStore();
        vi.clearAllMocks();
    });

    it('should initialize with default values', () => {
        expect(store.projectState).toBeNull();
        expect(store.isLoading).toBe(false);
        expect(store.error).toBeNull();
    });

    it('should handle addProject successfully', async () => {
        mockedProjectService.createProject.mockResolvedValue(mockProject);

        await store.addProject({
            name: 'Test Project',
            startDate: '2025-01-01',
            endDate: '2025-12-31',
            createdBy: 99,
        });

        expect(store.isLoading).toBe(false);
        expect(store.projectState).toEqual(mockProject);

        expect(store.error).toBeNull();
    });

    it('should handle addProject error', async () => {
        const mockError = new Error('Failed to create project');
        mockedProjectService.createProject.mockRejectedValue(mockError);
        mockedHandleError.mockReturnValue({ message: 'Error occurred', status: 500 });

        await store.addProject({
            name: 'Test Project',
            startDate: '2025-01-01',
            endDate: '2025-12-31',
            createdBy: 99,
        });

        expect(store.isLoading).toBe(false);
        expect(store.projectState).toBeNull();
        expect(store.error).toEqual({ message: 'Error occurred', status: 500 });
    });

    it('should fetch project by ID successfully', async () => {
        mockedProjectService.getProjectById.mockResolvedValue(mockProject);

        await store.fetchProjectById(1);

        expect(store.isLoading).toBe(false);
        expect(store.projectState).toEqual(mockProject);
        expect(store.error).toBeNull();
    });

    it('should handle fetchProjectById error', async () => {
        const mockError = new Error('Failed to fetch project');
        mockedProjectService.getProjectById.mockRejectedValue(mockError);
        mockedHandleError.mockReturnValue({ message: 'Error occurred', status: 404 });

        await store.fetchProjectById(1);

        expect(store.isLoading).toBe(false);
        expect(store.projectState).toBeNull();
        expect(store.error).toEqual({ message: 'Error occurred', status: 404 });
    });

    it('should add a column successfully', async () => {
    // On va créer un "mockProjectAvecColonne"
        const mockProjectWithColumn: ProjectResponse = {
            ...mockProject,
            boardColumns: [mockColumn],
        };

        mockedBoardColumnService.createColumn.mockResolvedValue(mockColumn);
        mockedProjectService.getProjectById.mockResolvedValue(mockProjectWithColumn);

        store.projectState = { ...mockProject, boardColumns: [] }; // État initial

        await store.addColumn({
            name: 'New Column',
            projectId: 1,
        });

        expect(store.isLoading).toBe(false);
        // On s’attend à voir la colonne ajoutée
        expect(store.projectState?.boardColumns).toContainEqual(mockColumn);
    });

    it('should handle createColumn error', async () => {
        const mockError = new Error('Failed to add column');
        mockedBoardColumnService.createColumn.mockRejectedValue(mockError);
        mockedHandleError.mockReturnValue({ message: 'Error occurred', status: 500 });

        store.projectState = { ...mockProject, boardColumns: [] };

        await store.addColumn({ name: 'New Column', projectId: 1 });

        expect(store.isLoading).toBe(false);
        expect(store.error).toEqual({ message: 'Error occurred', status: 500 });
    });

    it('should update a column name successfully', async () => {
    // On simule un projet avec 1 colonne existante
        const existingColumn: BoardColumnResponse = { ...mockColumn, name: 'Old Name' };
        store.projectState = {
            ...mockProject,
            boardColumns: [existingColumn],
        };

        await store.updateColumnName(1, 'New Name');

        expect(store.isLoading).toBe(false);
        expect(store.projectState?.boardColumns[0].name).toBe('New Name');
    });

    it('should remove a column successfully', async () => {

        const projectWithOneColumn: ProjectResponse = {
            ...mockProject,
            boardColumns: [mockColumn],
        };
        const projectWithoutColumn: ProjectResponse = {
            ...mockProject,
            boardColumns: [],
        };

        store.projectState = projectWithOneColumn;
        mockedBoardColumnService.deleteColumn.mockResolvedValue(projectWithoutColumn);

        await store.removeColumn(1);

        expect(store.isLoading).toBe(false);
        expect(store.projectState?.boardColumns).toHaveLength(0);
    });

    it('should add a task successfully', async () => {
    // On simule un projet avec une colonne
        const columnWithoutTask: BoardColumnResponse = { ...mockColumn, tasks: [] };
        store.projectState = { ...mockProject, boardColumns: [columnWithoutTask] };

        mockedTaskService.createTask.mockResolvedValue(mockTask);

        await store.addTask(1, {
            title: 'Task 1',
            dueDate: '2025-02-01',
            priority: 'LOW' as Priority,
            taskStatus: 'NOT_STARTED' as Status, 
            columnBoardId: 1,
        });

        expect(store.isLoading).toBe(false);

        const updatedCol = store.projectState?.boardColumns.find((col) => col.id === 1);
        expect(updatedCol?.tasks).toContainEqual(mockTask);
    });

    it('should update task details successfully', async () => {
    // On simule un projet avec 1 colonne et 1 tâche
        const columnWithOneTask: BoardColumnResponse = {
            ...mockColumn,
            tasks: [mockTask],
        };
        store.projectState = { ...mockProject, boardColumns: [columnWithOneTask] };

        // Tâche mise à jour
        const updatedTask: TaskResponse = {
            ...mockTask,
            title: 'Task updated',
            taskStatus: 'IN_PROGRESS' as Status,
        };
        mockedTaskService.updateTask.mockResolvedValue(updatedTask);

        await store.updateTaskDetails(1, 1, {
            title: 'Task updated',
            taskStatus: 'IN_PROGRESS' as Status,
        });

        expect(store.isLoading).toBe(false);
        const col = store.projectState?.boardColumns[0];
        expect(col?.tasks[0].title).toBe('Task updated');
        expect(col?.tasks[0].taskStatus).toBe('IN_PROGRESS');
    });

    it('should remove a task successfully', async () => {

        const columnWithOneTask: BoardColumnResponse = {
            ...mockColumn,
            tasks: [mockTask],
        };
        store.projectState = { ...mockProject, boardColumns: [columnWithOneTask] };

        mockedTaskService.deleteTask.mockResolvedValue();

        await store.removeTask(1, 1);

        expect(store.isLoading).toBe(false);
        const col = store.projectState?.boardColumns[0];
        expect(col?.tasks).toHaveLength(0);
    });

    it('should add a user to the project successfully', async () => {
        mockedProjectService.addUserInProject.mockResolvedValue("Utilisateur ajouté avec succès au projet");

        const result = await store.addUserToProject(mockUserRequest);

        expect(store.isLoading).toBe(false);
        expect(result).toEqual("Utilisateur ajouté avec succès au projet");
    });

    it('should handle addUserToProject error', async () => {
        const mockError = new Error('Failed to add user');
        mockedProjectService.addUserInProject.mockRejectedValue(mockError);
        mockedHandleError.mockReturnValue({ message: 'Error occurred', status: 500 });

        await store.addUserToProject({
            projectId: 1,
            userEmail: 'test.user@example.com',
            role: 'ADMIN' as Role,
        });

        expect(store.isLoading).toBe(false);
        expect(store.error).toEqual({ message: 'Error occurred', status: 500 });
    });
});
