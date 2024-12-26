<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useProjectStore } from '@/stores/projectStore';
import { useAuthStore } from '@/stores/authStore';
import { useRoute } from 'vue-router';
import { priorityTranslations, taskStatusTranslations, translateEnum } from '@/utils/translateEnum';

import type { ProjectResponse } from '@/types/interfaces/project';

const route = useRoute();
const projectStore = useProjectStore();
const authStore = useAuthStore();

const project = ref<ProjectResponse | null>(null);

onMounted(async () => {
    try {
        console.log(route.params.id);
        if (!route.params.id) {
            throw new Error('ID de projet non fourni dans les paramètres');
        }
        const paramId = Number(route.params.id);
        project.value = projectStore.projectState || null;

        if (!project.value) {
            if (!authStore.token) {
                throw new Error('Token d\'authentification manquant');
            }
            await projectStore.fetchProjectById(paramId, authStore.token);
        }
    } catch (err) {
        console.error('Erreur lors de la récupération du projet :', err);
    }
});
</script>

<template>

    <main class="bg-gray-100 min-h-screen py-6 px-4">
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-4 sm:p-6">

            <div class="border-b pb-4 mb-6">
                <h1 class="text-xl sm:text-2xl font-bold text-gray-800">{{ project?.name }}</h1>
                <p class="text-sm sm:text-base text-gray-600">{{ project?.description || "Aucune description fournie" }}</p>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
                <div>
                    <h2 class="font-semibold text-gray-700">Date de début</h2>
                    <p class="text-sm text-gray-600">{{ project?.startDate }}</p>
                </div>
                <div>
                    <h2 class="font-semibold text-gray-700">Date de fin</h2>
                    <p class="text-sm text-gray-600">{{ project?.endDate }}</p>
                </div>
                <div>
                    <h2 class="font-semibold text-gray-700">Créé le</h2>
                    <p class="text-sm text-gray-600">{{ project?.createdDate }}</p>
                </div>
                <div v-if="project?.updatedDate">
                    <h2 class="font-semibold text-gray-700">Mis à jour le</h2>
                    <p class="text-sm text-gray-600">{{ project?.updatedDate }}</p>
                </div>
            </div>

            <div class="border-t pt-4 mb-6">
                <h2 class="text-lg sm:text-xl font-bold text-gray-800">Créé par</h2>
                <div class="flex items-center mt-3">
                    <div class="flex-shrink-0 h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold">
                        {{ project?.createdBy.firstName[0] }}{{ project?.createdBy.lastName[0] }}
                    </div>
                    <div class="ml-4">
                        <p class="text-sm sm:text-base text-gray-800 font-semibold">
                            {{ project?.createdBy.firstName }} {{ project?.createdBy.lastName }}
                        </p>
                        <p class="text-xs sm:text-sm text-gray-600">{{ project?.createdBy.position }}</p>
                        <p class="text-xs sm:text-sm text-gray-600">{{ project?.createdBy.email }}</p>
                    </div>
                </div>
            </div>

            <div v-if="project?.users" class="border-t pt-4 mb-6">
                <h2 class="text-lg sm:text-xl font-bold text-gray-800">Utilisateurs associés</h2>
                <ul class="mt-3 space-y-3">
                    <li
                        v-for="user in project?.users"
                        :key="user.id"
                        class="bg-gray-50 rounded-lg p-4 flex items-center"
                    >
                        <div class="flex-shrink-0 h-8 w-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 font-bold">
                            {{ user.firstName[0] }}{{ user.lastName[0] }}
                        </div>
                        <div class="ml-4">
                            <p class="text-sm sm:text-base font-semibold text-gray-800">{{ user.firstName }} {{ user.lastName }}</p>
                            <p class="text-xs sm:text-sm text-gray-600">{{ user.email }}</p>
                        </div>
                    </li>
                </ul>
            </div>

            <div v-if="project?.columns" class="border-t pt-4">
                <h2 class="text-lg sm:text-xl font-bold text-gray-800">Colonnes</h2>
                <ul class="mt-3 space-y-3">
                    <li
                        v-for="column in project?.columns"
                        :key="column.id"
                        class="bg-gray-50 rounded-lg p-4"
                    >
                        <p class="text-sm sm:text-base font-semibold text-gray-800">{{ column.name }}</p>
                    </li>
                </ul>
            </div>

            <div v-if="!project?.users && !project?.columns" class="mt-6 text-center text-gray-600">
                <p>Aucun utilisateur ou colonne associé à ce projet.</p>
            </div>
        </div>
    </main>
</template>