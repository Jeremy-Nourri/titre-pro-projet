<script setup lang="ts">
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import FormInput from '@/components/ui/FormInput.vue';
import {
    Listbox,
    ListboxLabel,
    ListboxButton,
    ListboxOptions,
    ListboxOption,
} from '@headlessui/vue';
import { ChevronUpDownIcon } from '@heroicons/vue/16/solid';
import { CheckIcon } from '@heroicons/vue/20/solid';

import { toIsoDate } from '@/utils/toIsoDate';
import { Priority, Status, type TaskRequest } from '@/types/interfaces/task';
import { priorityLabels, statusLabels } from '@/utils/labelsTask';

const props = defineProps({
    columnId: {
        type: Number,
        required: true,
    },
    task: {
        type: Object as () => Partial<TaskRequest & { id?: number }>,
        default: () => ({}),
    },
});

const emit = defineEmits(['close', 'submit']);


const priorities = Object.entries(priorityLabels).map(([key, label]) => ({
    value: key,
    label, 
}));


const statuses = Object.entries(statusLabels).map(([key, label]) => ({
    value: key,
    label,
}));

const schema = yup.object({
    title: yup
        .string()
        .required('Le titre est requis')
        .max(50, 'Le titre ne doit pas dépasser 50 caractères'),
    detail: yup
        .string()
        .max(500, 'La description ne doit pas dépasser 500 caractères')
        .nullable(),
    dueDate: yup
        .date()
        .required("La date d'échéance est requise")
        .min(new Date(), 'La date doit être supérieure à la date actuelle'),
    priority: yup.string().required('La priorité est requise'),
    taskStatus: yup.string().required('Le statut est requis'),
});

const { handleSubmit, errors } = useForm<TaskRequest>({
    validationSchema: schema,
    initialValues: {
        title: props.task.title || '',
        detail: props.task.detail || '',
        dueDate: props.task.dueDate ? toIsoDate(props.task.dueDate) : '',
        priority: props.task.priority ||Priority.LOW,
        taskStatus: props.task.taskStatus || Status.NOT_STARTED,
    },
});

const { value: title } = useField<string>('title');
const { value: detail } = useField<string>('detail');
const { value: dueDate } = useField<string>('dueDate');
const { value: priority } = useField<string>('priority');
const { value: taskStatus } = useField<string>('taskStatus');

const onSubmit = handleSubmit(async (values) => {
    try {
        const task: TaskRequest = {
            ...values,
            columnBoardId: props.columnId,
        };
        emit('submit', task);
        emit('close');
    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire :', error);
    }
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
                {{ props.task.id ? 'Modifier une tâche' : 'Créer une tâche' }}
            </h2>
        </div>

        <div class="my-6 sm:mx-auto sm:w-full sm:max-w-sm h-full px-8">
            <form @submit.prevent="onSubmit">

                <FormInput
                    v-model="title"
                    label="Titre"
                    placeholder="Entrez le titre de la tâche"
                    :error="errors.title"
                />

                <FormInput
                    v-model="detail"
                    label="Détails"
                    placeholder="Entrez les détails de la tâche"
                    :error="errors.detail"
                />

                <FormInput
                    v-model="dueDate"
                    type="date"
                    label="Date d'échéance"
                    placeholder="Sélectionnez une date"
                    :error="errors.dueDate"
                />

                <div class="mt-4">
                    <Listbox v-model="priority" as="div" class="mt-4" required>
                        <ListboxLabel class="block font-medium text-xs md:text-base">
                            Priorité
                        </ListboxLabel>
                        <div class="relative">
                            <ListboxButton
                                class="grid w-full cursor-pointer grid-cols-1 rounded-md bg-white py-2 pl-3 pr-2
                       text-left text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300
                       focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-bluecolor"
                            >
                                <span class="col-start-1 row-start-1 flex items-center gap-3 pr-6"> 
                                    <span class="block truncate md:md:text-sm/6 text-[12px]">
                                        {{ priorities.find(p => p.value === priority)?.label || 'Sélectionnez une priorité' }}
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
                                        v-for="option in priorities"
                                        :key="option.value"
                                        v-slot="{ active, selected }"
                                        :value="option.value"
                                    >
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
                </div>

                <div class="mt-4">
                    <Listbox v-model="taskStatus" as="div" class="mt-4" required>
                        <ListboxLabel class="block font-medium text-xs md:text-base">
                            Statut
                        </ListboxLabel>
                        <div class="relative">
                            <ListboxButton
                                class="grid w-full cursor-pointer grid-cols-1 rounded-md bg-white py-2 pl-3 pr-2
                       text-left text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300
                       focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-bluecolor"
                            >
                                <span class="col-start-1 row-start-1 flex items-center gap-3 pr-6">
                                    <span class="block truncate md:md:text-sm/6 text-[12px]">
                                        {{ statuses.find(s => s.value === taskStatus)?.label || 'Sélectionnez un statut' }}
                                    </span>
                                </span>
                                <ChevronUpDownIcon class="col-start-1 row-start-1 size-4 self-center justify-self-end text-gray-500 sm:size-4" aria-hidden="true" />
                            </ListboxButton>
                            <ListboxOptions
                                class="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white
                       py-1 shadow-lg ring-1 ring-black/5 focus:outline-none"
                            >
                                <ListboxOption
                                    v-for="option in statuses"
                                    :key="option.value"
                                    v-slot="{ active, selected }"
                                    :value="option.value"
                                >
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
                        </div>
                    </Listbox>
                </div>

                <div class="my-6">
                    <button
                        type="submit"
                        class="w-full justify-center rounded-md bg-blue-500 ease-in duration-300
                   hover:opacity-70 py-2 text-white"
                    >
                        Enregistrer
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
