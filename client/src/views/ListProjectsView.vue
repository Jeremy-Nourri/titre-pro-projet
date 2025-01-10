<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import ProjectCard from '@/components/ProjectCard.vue';
import UserProjectCard from '@/components/UserProjectCard.vue';

const authStore = useAuthStore();

const fetchUserData = async () => {
    try {
        await authStore.fetchUser();
    } catch (err) {
        console.error('Erreur lors de la récupération des informations utilisateur:', err);
    }
};

onMounted(() => {
    fetchUserData();
});

const createdProjects = computed(() => authStore.user?.createdProjects ?? []);
const userProjects = computed(() => authStore.user?.userProjects ?? []);
</script>

<template>
    <main class="bg-slate-50 h-screen p-4">
        <h4 class="mb-4 text-bluecolor">Mes projets créés</h4>
        <div v-if="createdProjects.length > 0" class="flex gap-3">
            <ProjectCard
                v-for="project in createdProjects"
                :key="project.id"
                :project="project"
            />
        </div>
        <div v-else>Aucun projet créé.</div>

        <h4 class="mb-4 mt-8 text-bluecolor">Mes projets</h4>
        <div v-if="userProjects.length > 0" class="flex gap-3">
            <UserProjectCard
                v-for="project in userProjects"
                :key="project.id"
                :project="project"
            />
        </div>
        <div v-else>
            Aucun projet en tant que participant.
        </div>
    </main>
</template>
