<script setup lang="ts">
import { useRouter } from 'vue-router';
import type { UserProjectResponse } from '@/types/interfaces/userProject';

const props = defineProps<{
  project: UserProjectResponse;
}>();

const router = useRouter();

const redirectToProject = () => {
    router.push({
        name: 'ProjectBoardView',
        params: { id: props.project.projectId },
    });
}
</script>

<template>
    <div
        class="bg-white shadow-md rounded p-4 hover:shadow-lg cursor-pointer transition"
        @click="redirectToProject"
    >
        <h4 class="">{{ project.projectName }}</h4>

        <p class="p-text-small mb-1 text-gray-600">
            Description : {{ project.projectDescription }}
        </p>
        <p class="p-text-small mb-1 text-gray-600">
            Date de fin : {{ project.endDate }}
        </p>
        
        <p class="p-text-small mb-1 text-gray-600">
            Ajouté le : {{ project.userAddedAt }}
        </p>
    </div>
</template>

