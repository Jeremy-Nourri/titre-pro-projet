<script setup lang="ts">
import { useRouter } from 'vue-router';
import type { ProjectResponse } from '@/types/interfaces/project';

const props = defineProps<{
  project: ProjectResponse;
}>();

const router = useRouter();

function redirectToProject() {
    router.push({
        name: 'ProjectBoardView',
        params: { id: props.project.id },
    });
}
</script>

<template>
    <div
        class="bg-white shadow-md rounded-md p-4 hover:shadow-lg cursor-pointer transition"
        @click="redirectToProject"
    >
        <h4 class="">{{ project.name }}</h4>
        <p class="p-text-small text-gray-600">
            Description : {{ project.description }}
        </p>
        <p class="p-text-small text-gray-600">
            Date de fin : {{ project.endDate }}
        </p>
    </div>
</template>
