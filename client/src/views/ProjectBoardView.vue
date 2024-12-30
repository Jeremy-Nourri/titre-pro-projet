<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import { useProjectStore } from '@/stores/projectStore';
import { useRoute } from 'vue-router';
import FormInput from '@/components/ui/FormInput.vue';
import BoardColumn from '@/components/BoardColumn.vue';
import ReusableModal from '@/components/ui/ReusableModal.vue';
import { PlusCircleIcon } from '@heroicons/vue/20/solid'
import type { BoardColumnRequest } from '@/types/interfaces/boardColumn';

const projectStore = useProjectStore();

const route = useRoute();

const showAddColumnForm = ref(false);

const schema = yup.object({
    name: yup
        .string()
        .required('Le titre est requis')
        .max(50, 'Le titre ne doit pas dépasser 50 caractères'),
});

const { handleSubmit, errors } = useForm<BoardColumnRequest>({
    validationSchema: schema,
});

const { value: name } = useField<string>('name');

const canAddColumn = computed(() => {
    const boardColumns = projectStore.projectState?.boardColumns;
    return boardColumns && boardColumns.length < 4 && !showAddColumnForm.value;
});


onMounted(async () => {
    try {
        if (!route.params.id) {
            throw new Error('ID de projet non fourni dans les paramètres');
        }
        const paramId = Number(route.params.id);

        await projectStore.fetchProjectById(paramId);
    }
    catch (err) {
        console.error('Erreur lors de la récupération du projet :', err);
    }
});

const onSubmit = handleSubmit(async (values) => {
    try {
        const paramId = Number(route.params.id);

        const newColumn: BoardColumnRequest = {
            name: values.name,
            projectId: paramId,
            tasks: []
        }

        await projectStore.addColumn(newColumn);
        showAddColumnForm.value = false;
        
    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire :', error);

    }
});
</script>

<template>
    <main class="bg-gray-100 min-h-screen md:p-7 p-2">
        <h1 class="text-center mb-8">
            Projet : {{ projectStore.projectState?.name || 'Chargement...' }}
        </h1>
        <div v-if="canAddColumn" class="flex justify-end mx-auto mb-6">
            <button
                class="group justify-center rounded-sm hover:white outline outline-4 outline-bluecolor bg-bluecolor hover:bg-white
                 ease-in duration-300 lg:p-1.5 p-0.5"
                title="Ajouter une colonne"
                @click="showAddColumnForm = true"
                
            >
                <PlusCircleIcon class="xl:size-6 size-5 text-white group-hover:text-bluecolor ease-in duration-300" />
            </button>
        </div>
        <div v-else class="mb-6">
            <p class="text-gray-500 md:text-sm md:text-right text-xs text-center">
                Vous avez atteint la limite de 4 colonnes
            </p>
        </div>

        <div v-if="projectStore.isLoading" class="text-center">
            <p>Chargement des colonnes...</p>
        </div>

        <div v-else class="min-h-72 max-h-full overflow-auto bg-slate-200 lg:flex lg:flex-nowrap md:flex md:flex-wrap md:justify-center gap-4 mx-auto p-4">
            <BoardColumn
                v-for="column in projectStore.projectState?.boardColumns || []"
                :key="column.id"
                :column-id="column.id"
                :title="column.name"
                :tasks="column.tasks"
            />
        </div>

        <div class="mt-6 text-center">
            <ReusableModal v-model="showAddColumnForm">
                <template #default>
                    <div class="p-4">
                        <h2 class="text-lg font-bold mb-4">Ajouter une colonne</h2>
                        <form @submit.prevent="onSubmit">
                            <FormInput
                                v-model="name"
                                label="Nom de la colonne"
                                placeholder="Entrez le nom de la colonne"
                                :error="errors.name"
                            />
                            <button type="submit" class="bg-bluecolor text-white button-base mt-4">
                                Ajouter
                            </button>
                        </form>
                    </div>
                </template>
            </ReusableModal>
        </div>
    </main>
</template>