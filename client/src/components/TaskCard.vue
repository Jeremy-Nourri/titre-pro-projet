<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useProjectStore } from '@/stores/projectStore';
import ReusableModal from '@/components/ui/ReusableModal.vue';
import TaskForm from './TaskForm.vue';
import { priorityLabels, statusLabels } from '@/utils/labelsTask';
import type { TaskRequest, TaskResponse } from '@/types/interfaces/task';

const props = defineProps<{
  columnId: number;
  task: TaskResponse
}>();


const emit = defineEmits(['task-updated', 'task-deleted']);

const authStore = useAuthStore();
const projectStore = useProjectStore();

const showDetailsModal = ref(false);
const showUpdateForm = ref(false);
const showDeleteConfirmation = ref(false);

const handleUpdateTask = async (updatedTask: TaskRequest) => {
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        } else if (!authStore?.token) {
            throw new Error('Token non fourni');
        } 
        await projectStore.updateTaskDetails(
            props.columnId,
            props.task.id,
            updatedTask,
            authStore.token
        );
        emit('task-updated');
        showUpdateForm.value = false;
        showDetailsModal.value = false;
    } catch (error) {
        console.error('Erreur lors de la mise à jour de la tâche :', error);
    }
};

const handleDeleteTask = async () => {
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        } else if (!authStore?.token) {
            throw new Error('Token non fourni');
        } 
        await projectStore.removeTask(props.columnId, props.task.id, authStore.token);
        emit('task-deleted', props.task.id);
        showDeleteConfirmation.value = false;
        showDetailsModal.value = false;
    } catch (error) {
        console.error('Erreur lors de la suppression de la tâche :', error);
    }
};
</script>

<template>
    <div
        class="task-card bg-white shadow rounded-md p-4 mb-4 cursor-pointer hover:shadow-lg"
        @click="showDetailsModal = true"
    >
        <h4 class="">{{ task.title }}</h4>
        <p class="p-text text-gray-600 truncate">{{ task.detail }}</p>
    </div>

    <ReusableModal v-model="showDetailsModal">
        <div class="p-4">
            <h4 class="mb-2">{{ task.title }}</h4>
            <p class="p-text text-gray-700">
                <strong>Détails :</strong> {{ task.detail || 'Aucun détail fourni' }}
            </p>
            <p v-if="task" class="p-text text-gray-700">
                <strong>Priorité :</strong> {{ priorityLabels[task.priority] }}
            </p>
            <p v-if="task" class="p-text text-gray-700">
                <strong>Statut :</strong> {{ statusLabels[task.taskStatus] }}
            </p>
            <p v-if="task.dueDate" class="p-text text-gray-700">
                <strong>Date d'échéance :</strong> {{ task.dueDate }}
            </p>
            <div class="flex justify-end gap-4 mt-4">
                <button
                    class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                    @click="showUpdateForm = true"
                >
                    Modifier
                </button>
                <button
                    class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    @click="showDeleteConfirmation = true"
                >
                    Supprimer
                </button>
            </div>
        </div>
    </ReusableModal>

    <ReusableModal v-model="showUpdateForm">
        <TaskForm
            :column-id="columnId"
            :task="task"
            @close="showUpdateForm = false"
            @submit="handleUpdateTask"
        />
    </ReusableModal>

    <ReusableModal v-model="showDeleteConfirmation">
        <div class="p-4 text-center">
            <p class="text-lg font-bold mb-4">
                Êtes-vous sûr de vouloir supprimer cette tâche ?
            </p>
            <div class="flex justify-center gap-4">
                <button
                    class="px-4 py-2 bg-gray-400 rounded hover:bg-gray-400"
                    @click="showDeleteConfirmation = false"
                >
                    Annuler
                </button>
                <button
                    class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    @click="handleDeleteTask"
                >
                    Supprimer
                </button>
            </div>
        </div>
    </ReusableModal>
</template>
