import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { ProjectRequest, ProjectResponse } from '@/types/interfaces/project';
import { getProjectById } from '@/services/projectService';
import { useAuthStore } from './authStore';
import { useRouter } from 'vue-router';
import { createProject } from '@/services/projectService';

export const useProjectStore = defineStore('project', () => {

    const router = useRouter();

    const authStore = useAuthStore();

    const projectState = ref<ProjectResponse>();
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const resetError = () => {
        error.value = null;
    };

    const addProject = async (project: ProjectRequest, token: string): Promise<ProjectResponse | string> => {
        isLoading.value = true;
        resetError(); 

        authStore.checkTokenInStore();		

        const response = await createProject(project, token);
        if (typeof response === 'string') {
            error.value = response;
        } else {
            projectState.value = response;
            router.push({ 
                name: 'ProjectView', 
                params: { id: projectState.value.id }
            });
        }

        isLoading.value = false;
        return response;
    };

    const fetchProjectById = async (projectId: number, token: string): Promise<ProjectResponse | string> => {
        
        isLoading.value = true;
        resetError();

        const response = await getProjectById(projectId, token)

        if (typeof response ==='string') {
            error.value = response;
        } else {
            projectState.value = response;
        }

        isLoading.value = false;
        return response;
    }

    return {
        projectState,
        isLoading,
        error,
        addProject,
        fetchProjectById,
    };
});