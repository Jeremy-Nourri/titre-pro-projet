<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import { useProjectStore } from '@/stores/projectStore';
import { useAuthStore } from '@/stores/authStore';
import { useRoute } from 'vue-router';
import FormInput from '@/components/ui/FormInput.vue';
import BoardColumn from '@/components/BoardColumn.vue';
import ReusableModal from '@/components/ui/ReusableModal.vue';
import type { BoardColumnRequest } from '@/types/interfaces/boardColumn';

const projectStore = useProjectStore();
const authStore = useAuthStore();

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

onMounted(async () => {
    try {
        console.log(route.params.id);
        if (!route.params.id) {
            throw new Error('ID de projet non fourni dans les paramètres');
        }
        const paramId = Number(route.params.id);

        if (!authStore.token) {
            throw new Error('Token d\'authentification manquant');
        }
        await projectStore.fetchProjectById(paramId, authStore.token);
    }
    catch (err) {
        console.error('Erreur lors de la récupération du projet :', err);
    }
});

const onSubmit = handleSubmit(async (values) => {
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        } else if (!authStore?.token) {
            throw new Error('Token non fourni');
        }

        const paramId = Number(route.params.id);

        const newColumn: BoardColumnRequest = {
            name: values.name,
            projectId: paramId,
            tasks: []
        }

        await projectStore.addColumn(newColumn, authStore.token);
        showAddColumnForm.value = false;
    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire :', error);

    }
});
</script>

<template>
    <div class="bg-gray-100 min-h-screen p-4">
        <h1 class="text-center mb-8">
            Projet : {{ projectStore.projectState?.name || 'Chargement...' }}
        </h1>

        <div v-if="projectStore.isLoading" class="text-center">
            <p>Chargement des colonnes...</p>
        </div>

        <div v-else class="w-11/12 bg-slate-500 md:flex gap-4 mx-auto p-4">
            <BoardColumn
                v-for="column in projectStore.projectState?.boardColumns || []"
                :key="column.id"
                :column-id="column.id"
                :title="column.name"
                :tasks="column.tasks"
            />
        </div>

        <div class="mt-6 text-center">
            <button
                v-if="!showAddColumnForm"
                class="button-secondary "
                @click="showAddColumnForm = true"
            >
                Ajouter une colonne
            </button>
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
                            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md mt-4">
                                Ajouter
                            </button>
                        </form>
                    </div>
                </template>
            </ReusableModal>
        </div>
    </div>
</template>&