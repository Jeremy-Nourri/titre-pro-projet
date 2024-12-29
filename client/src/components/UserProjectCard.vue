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
        <h3 class="text-lg font-bold">{{ project.projectName }}</h3>
        <p class="text-sm text-gray-600">
            <strong>Ajouté le :</strong> {{ project.userAddAt }}
        </p>
    </div>
</template>

