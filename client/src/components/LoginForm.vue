<script setup lang="ts">
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import LogoProjectFlow from '@/assets/img/logo-project-flow.webp';
import FormInput from './ui/FormInput.vue';
import type { LoginRequest } from '@/types/interfaces/login';
import { useAuthStore } from '@/stores/authStore';
import { onMounted, onBeforeUnmount } from 'vue';
import router from '@/router';


const emit = defineEmits<{
  (event: 'changeComponent', component: string): void;
}>();

const authStore = useAuthStore();

onMounted(() => {
    authStore.resetError();
});

onBeforeUnmount(() => {
    authStore.resetError();
});

const schema = yup.object({
    email: yup.string().email('Email invalide').required('L\'email est requis'),
    password: yup
        .string()
        .min(8, 'Le mot de passe doit contenir au moins 8 caractères')
        .matches(
            /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$/,
            'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial'
        )
        .required('Le mot de passe est requis'),
});

const { handleSubmit, errors } = useForm<LoginRequest>({
    validationSchema: schema,
});

const { value: email } = useField<string>('email');
const { value: password } = useField<string>('password');

const onSubmit = handleSubmit(async (values: LoginRequest) => {
    authStore.resetError();
    await authStore.signin(values);
    if (authStore.token) {
        console.log('Login successful, redirecting to dashboard');
        router.push('/dashboard');
    }
});

</script>

<template>
    <div class="bg-white md:w-3/5 w-11/12 mx-auto rounded-2xl shadow-2xl pb-4">
        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
            <img class="mx-auto h-32 w-auto" :src="LogoProjectFlow" alt="Your Company" />
            <h2 class="text-center tracking-tight text-primary">Se connecter</h2>
        </div>
        <div class="my-6 sm:mx-auto sm:w-full sm:max-w-sm h-full px-8">
            <form @submit.prevent="onSubmit">

                <FormInput
                    v-model="email" type="email" label="Email" placeholder="Entrez votre email" autocomplete="on"
                    :error="errors.email" 
                />

                <FormInput
                    v-model="password" class="mb-1" type="password" label="Mot de passe"
                    placeholder="Entrez votre mot de passe" autocomplete="off" :error="errors.password" 
                />

                <p v-if="authStore.error" class="my-4 py-2 bg-danger text-center text-white md:text-xs/6 text-[10px]">
                    {{ authStore.error.message }}
                </p>
            
                <div class="my-4 ">
                    <button
                        type="submit"
                        class=" button-primary">
                        Se connecter
                    </button>
                </div>
            </form>
        </div>
        <button class="block mx-auto md:text-sm/6 text-[12px] text-bluecolor hover:opacity-80 ease-in duration-300 underline" @click="emit('changeComponent', 'register')" >
            Créer un compte
        </button>

    </div>


</template>
