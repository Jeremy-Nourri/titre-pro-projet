<script setup lang="ts">
import LoginForm from '@/components/LoginForm.vue';
import RegisterForm from '@/components/RegisterForm.vue';
import { ref , computed} from 'vue';
import { useAuthStore } from '@/stores/authStore'; 

const authStore = useAuthStore();

const isAuthenticated = computed(() => !!authStore.user || !!authStore.token);

const component = ref<string>('login');


</script>

<template>
    <main class=" bg-bluecolor w-screen p-4">
        <RegisterForm v-if="component === 'register' && !isAuthenticated" @change-component="component = $event" />
        <LoginForm v-if="component === 'login' && !isAuthenticated" @change-component="component = $event"/>
    </main>
</template>
