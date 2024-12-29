<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useUserStore } from '@/stores/userStore';
import ProjectCard from '@/components/ProjectCard.vue';
import UserProjectCard from '@/components/UserProjectCard.vue';

const authStore = useAuthStore();
const userStore = useUserStore();

const fetchUserData = async () =>{
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        } else if (!authStore?.token) {
            throw new Error('Token non fourni');
        } 

        await userStore.getUserById(authStore.user.id, authStore.token);
    } catch (err) {
        console.error('Erreur lors de la récupération des informations utilisateur:', err);
    }
}

onMounted(() => {
    fetchUserData();
});

const createdProjects = computed(() => userStore.user?.createdProjects ?? []);
const userProjects = computed(() => userStore.user?.userProjects ?? []);

</script>

<template>
    <main class="bg-slate-50 h-screen p-4">
        <h4 class="mb-4">Mes projets créés</h4>
        <div v-if="createdProjects.length > 0" class="flex">
            <ProjectCard
                v-for="project in createdProjects"
                :key="project.id"
                :project="project"
            />
        </div>
        <div v-else>Aucun projet créé.</div>

        <h4 class="mb-4">Mes projets en tant que participant</h4>
        <div v-if="userProjects.length > 0" class="flex">
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
