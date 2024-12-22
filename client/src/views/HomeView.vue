<script setup lang="ts">
import LoginForm from '@/components/LoginForm.vue';
import RegisterForm from '@/components/RegisterForm.vue';
import { ref } from 'vue';

const component = ref<string>('login');


</script>

<template>
    <main class=" bg-bluecolor w-screen p-4">
        <RegisterForm v-if="component === 'register'" @change-component="component = $event" />
        <LoginForm v-if="component === 'login'" @change-component="component = $event"/>
    </main>
</template>
