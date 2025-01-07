<script setup lang="ts">
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import FormInput from '@/components/ui/FormInput.vue';
import  { type UserProjectRequest } from '@/types/interfaces/userProject';
import {
    Listbox,
    ListboxLabel,
    ListboxButton,
    ListboxOptions,
    ListboxOption,
} from '@headlessui/vue';
import { ChevronUpDownIcon } from '@heroicons/vue/16/solid';
import { CheckIcon } from '@heroicons/vue/20/solid';
import type { ResponseError } from '@/types/responseError';

const props = defineProps<{
  projectId: number;
  errorsResponse: ResponseError | null
}>();

const emit = defineEmits(['close', 'submit']);

const roles = [
    { value: 'ADMIN', label: "Administrateur"},
    { value: 'MEMBER', label: "Participant"}
]


const schema = yup.object({
    userEmail: yup.string().email('Email invalide').required('L\'email est requis'),
    role: yup.string().required('La position est requise'),
});

const { handleSubmit, errors } = useForm<UserProjectRequest>({
    validationSchema: schema,
});

const { value: userEmail } = useField<string>('userEmail');
const { value: role } = useField<string>('role');

const onSubmit = handleSubmit((values) => {
    const userProject: UserProjectRequest = {
        projectId: props.projectId,
        userEmail: values.userEmail,
        role: values.role,
    };
    emit('submit', userProject);

});


</script>

<template>
    <div class="bg-white mx-auto rounded-2xl shadow-2xl md:p-12 p-2">
        <button
            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
            @click="emit('close')"
        >
            <svg
                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                stroke-width="2" stroke="currentColor" class="w-6 h-6"
            >
                <path
                    stroke-linecap="round" stroke-linejoin="round"
                    d="M6 18L18 6M6 6l12 12" />
            </svg>
        </button>

        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
            <h2 class="text-center tracking-tight text-primary">
                Ajouter un utilisateur au projet
            </h2>
        </div>

        <div class="my-6 sm:mx-auto sm:w-full sm:max-w-sm h-full px-8">
            <form @submit.prevent="onSubmit">

                <FormInput
                    v-model="userEmail"
                    label="Email"
                    placeholder="Entrez l'email de l'utilisateur"
                    :error="errors.userEmail"
                />

                <div class="mt-4">
                    <Listbox v-model="role" as="div" class="mt-4" required>
                        <ListboxLabel class="block font-medium text-xs md:text-base">
                            Rôle
                        </ListboxLabel>
                        <div class="relative">
                            <ListboxButton
                                class="grid w-full cursor-pointer grid-cols-1 rounded-md bg-white py-2 pl-3 pr-2
                                text-left text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300
                                focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-bluecolor"
                            >
                                <span class="col-start-1 row-start-1 flex items-center gap-3 pr-6"> 
                                    <span class="block truncate md:text-sm text-[12px]">
                                        {{ roles.find(r => r.value === role)?.label || 'Sélectionnez un rôle' }}
                                    </span>
                                </span>
                                <ChevronUpDownIcon class="col-start-1 row-start-1 size-4 self-center justify-self-end text-gray-500 sm:size-4" aria-hidden="true" />
                            </ListboxButton>

                            <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100" leave-to-class="opacity-0">
                                <ListboxOptions
                                    class="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white py-1
                                    shadow-lg ring-1 ring-black/5 focus:outline-none"
                                >
                                    <ListboxOption
                                        v-for="option in roles"
                                        :key="option.value"
                                        v-slot="{ active, selected }"
                                        :value="option.value"
                                    >
                                        <li :class="[active ? 'bg-bluecolor text-white outline-none' : 'text-gray-900', 'relative cursor-pointer select-none py-2 pl-3 pr-9 md:text-sm text-[12px]']">
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
                </div>
                <p v-if="props.errorsResponse" class="my-4 py-2 bg-danger text-center text-white md:text-xs/6 text-[10px]">
                    {{ props.errorsResponse.message }}                
                </p>

                <div class="mt-4">
                    <button type="submit" class="button-primary">
                        Ajouter
                    </button>
                </div>


            </form>

        </div>


    </div>
</template>



<style scoped>

</style>