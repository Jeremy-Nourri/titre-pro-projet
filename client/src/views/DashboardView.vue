<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

const userFirstName = authStore.user?.firstName

</script>

<template>
    <main class="h-screen flex flex-col items-center bg-slate-50">
        <p class="p-text mt-10">Bienvenue <span class="text-bluecolor font-bold first-letter:uppercase">{{ userFirstName }}</span> sur votre Dashboard Project Flow</p>
        <p class="p-text">Retrouvez vos projets, la liste des membres associés à vos projets..</p>
    </main>
</template>