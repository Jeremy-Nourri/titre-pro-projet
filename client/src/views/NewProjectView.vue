<script setup lang="ts">
import { useForm, useField } from 'vee-validate';
import * as yup from 'yup';
import FormInput from '@/components/ui/FormInput.vue';
import type { Project, ProjectRequest } from '@/types/interfaces/project';
import { useAuthStore } from '@/stores/authStore';
import { useProjectStore } from '@/stores/projectStore';

const authStore = useAuthStore();
const projectStore = useProjectStore();


const schema = yup.object({
    name: yup
        .string()
        .required('Le nom est requis')
        .max(50, 'Le nom ne doit pas dépasser 50 caractères'),
    description: yup
        .string()
        .max(500, 'La description ne doit pas dépasser 500 caractères')
        .nullable(),
    startDate: yup
        .date()
        .required('La date de début est requise'),
    endDate: yup
        .date()
        .required('La date de fin est requise')
        .min(yup.ref('startDate'), 'La date de fin doit être après la date de début'),
});

const { handleSubmit, errors } = useForm<Project>({
    validationSchema: schema,
});

const { value: name } = useField<string>('name');
const { value: description } = useField<string>('description');
const { value: startDate } = useField<string>('startDate');
const { value: endDate } = useField<string>('endDate');

const onSubmit = handleSubmit(async (values: Project) => {
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        }

        const project: ProjectRequest = {
            ...values,
            createdBy: authStore.user.id,
        }

        await projectStore.addProject(project)

    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire:', error);
    }
});
</script>

<template>
    <main class="bg-slate-50 h-screen">
        <form class="space-y-4 p-4 md:w-10/12 lg:w-2/4 mx-auto" @submit.prevent="onSubmit">
            <FormInput
                v-model="name"
                name="name"
                label="Nom du projet"
                placeholder="Entrez le nom du projet"
                :error="errors.name"
            />
      
            <FormInput
                v-model="description"
                name="description"
                label="Description"
                placeholder="Entrez une description (facultatif)"
                :error="errors.description"
            />
      
            <FormInput
                v-model="startDate"
                name="startDate"
                label="Date de début"
                type="date"
                :error="errors.startDate"
            />
      
            <FormInput
                v-model="endDate"
                name="endDate"
                label="Date de fin"
                type="date"
                :error="errors.endDate"
            />
      
            <button type="submit" class="button-primary">Créer le projet</button>
        </form>
    </main>
</template>
