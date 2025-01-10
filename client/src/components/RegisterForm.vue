<script setup lang="ts">

import { Listbox, ListboxButton, ListboxLabel, ListboxOption, ListboxOptions } from '@headlessui/vue'
import { ChevronUpDownIcon } from '@heroicons/vue/16/solid'
import { CheckIcon } from '@heroicons/vue/20/solid'
import { useForm, useField } from 'vee-validate';
import { onMounted, onBeforeUnmount } from 'vue';
import * as yup from 'yup';
import { Position, type UserRequest } from '@/types/interfaces/user';
import LogoProjectFlow from '@/assets/img/logo-project-flow.webp';
import FormInput from './ui/FormInput.vue';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();

const emit = defineEmits<{ (event: 'change-component', component: string): void }>();

onMounted(() => {
    authStore.resetError();
});

onBeforeUnmount(() => {
    authStore.resetError();
});

setInterval(() => {
    authStore.resetError();
}, 30000);

const positions = Object.entries(Position).map(([value, label]) => ({ value, label }));

const schema = yup.object({
    firstName: yup.string().required('Le prénom est requis'),
    lastName: yup.string().required('Le nom est requis'),
    email: yup.string().email('Email invalide').required('L\'email est requis'),
    password: yup
        .string()
        .min(8, 'Le mot de passe doit contenir au moins 8 caractères')
        .matches(
            /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$/,
            'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial'
        )
        .required('Le mot de passe est requis'),
    confirmPassword: yup
        .string()
        .oneOf([yup.ref('password')], 'Les mots de passe ne correspondent pas')
        .required('La confirmation du mot de passe est requise'),
    position: yup.string().required('La position est requise'),
});

const { handleSubmit, errors } = useForm<UserForm>({
    validationSchema: schema,
});

const { value: firstName } = useField<string>('firstName');
const { value: lastName } = useField<string>('lastName');
const { value: email } = useField<string>('email');
const { value: password } = useField<string>('password');
const { value: confirmPassword } = useField<string>('confirmPassword');
const { value: position } = useField<string>('position');

type UserForm = UserRequest & { confirmPassword: string };


const onSubmit = handleSubmit(async (values: UserForm) => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { confirmPassword, ...userData } = values;
    await authStore.addUser(userData as UserRequest);
    if (!authStore.error) {
        emit('change-component', 'login');
    }
});

</script>

<template>
    <div class="bg-white md:w-3/5 w-11/12 mx-auto rounded-2xl shadow-2xl pb-4">
        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
            <img class="mx-auto h-32 w-auto" :src="LogoProjectFlow" alt="Your Company" />
            <h3 class="text-center tracking-tight text-primary">Créer un compte</h3>
        </div>
        <div class="my-6 sm:mx-auto sm:w-full sm:max-w-sm h-full px-8">
            <form @submit.prevent="onSubmit">

                <FormInput v-model="firstName" label="Prénom" placeholder="Entrez votre prénom" autocomplete="on" :error="errors.firstName" />
                
                <FormInput v-model="lastName" label="Nom" placeholder="Entrez votre nom" autocomplete="on" :error="errors.lastName" />

                <Listbox v-model="position" as="div" class="mt-4" required>
                    <ListboxLabel class="block font-medium text-xs md:text-base">Poste</ListboxLabel>
                    <div class="relative">

                        <ListboxButton class="grid w-full cursor-pointer grid-cols-1 rounded-md bg-white py-2 pl-3 pr-2 text-left text-gray-900 outline outline-1 -outline-offset-1 outline-gray-500 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-bluecolor ">
                            <span class="col-start-1 row-start-1 flex items-center gap-3 pr-6"> 
                                <span class="block truncate text-gray-400 md:md:text-sm/6 text-[12px]">{{ positions.find(p => p.value === position)?.label || 'Sélectionnez un poste' }}</span>
                            </span> 
                            <ChevronUpDownIcon class="col-start-1 row-start-1 size-4 self-center justify-self-end text-gray-500 sm:size-4" aria-hidden="true" />
                        </ListboxButton>

                        <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
                            <ListboxOptions class="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white py-1 shadow-lg ring-1 ring-black/5 focus:outline-none">

                                <ListboxOption v-for="option in positions" :key="option.value" v-slot="{ active, selected }" as="template" :value="option.value">
                                    <li :class="[active ? 'bg-bluecolor text-white outline-none' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-3 pr-9 md:md:text-sm/6 text-[12px]']">
                                        <div class="flex items-center">
                                            <span :class="[selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate']">{{ option.label }}</span>
                                        </div>
                                        <span v-if="selected" :class="[active ? 'text-white' : 'text-bluecolor', 'absolute inset-y-0 right-0 flex items-center pr-4']">
                                            <CheckIcon class="size-4" aria-hidden="true" />
                                        </span>
                                    </li>
                                </ListboxOption>
                            </ListboxOptions>
                        </transition>
                    </div>
                </Listbox>
                
                <FormInput v-model="email" type="email" label="Email" placeholder="Entrez votre email" autocomplete="on" :error="errors.email" />
                
                <FormInput v-model="password" class="mb-1" type="password" label="Mot de passe" placeholder="Entrez votre mot de passe" autocomplete="off" :error="errors.password" />
                <p class="pl-1 md:text-[10px] text-[9px]">Le mot de passe doit comporter un minimum de 8 caractères, une majuscule, une minuscule, un nombre et un caractère spécial</p>
                
                <FormInput v-model="confirmPassword" type="password" label="Confirmation du mot de passe" placeholder="Entrez votre mot de passe" autocomplete="off" :error="errors.confirmPassword" />

                <p v-if="authStore.error" class="my-4 py-2 bg-danger text-center text-white md:text-xs/6 text-[10px]">
                    {{ authStore.error.message }}                </p>

                <div class="my-6 ">
                    <button type="submit" class="button-primary">
                        Enregistrer
                    </button>
                </div>
            </form>
        </div>
        <button class="block mx-auto md:text-sm/6 text-[12px] text-bluecolor hover:opacity-80 ease-in duration-300 underline" @click="emit('change-component', 'login')" >
            Se connecter
        </button>
    </div>


</template>
