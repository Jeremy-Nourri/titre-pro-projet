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
        class="bg-white shadow-md rounded p-4 hover:shadow-lg cursor-pointer transition"
        @click="redirectToProject"
    >
        <h4 class="">{{ project.name }}</h4>
        <p class="p-text text-gray-600">
            <strong>Description :</strong> {{ project.description }}
        </p>
        <p class="text-[0.7rem] sm:text-sm md:text-md lg:text-lg xl:text-lg text-gray-600">
            <strong>Date de début :</strong> {{ project.startDate }}
        </p>
    </div>
</template>
