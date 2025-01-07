<script setup lang="ts">
import { computed } from 'vue';
import { RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'; 
import NavBanner from './components/NavBanner.vue';

const authStore = useAuthStore();

const isAuthenticated = computed(() => !!authStore.user && !!authStore.token);

</script>

<template>
    <header v-if="isAuthenticated">
        <NavBanner />
    </header>

    <RouterView />
</template>
