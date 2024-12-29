<script setup lang="ts">
import { computed, ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useProjectStore } from '@/stores/projectStore';
import TaskForm from '@/components/TaskForm.vue';
import TaskCard from '@/components/TaskCard.vue';
import ReusableModal from '@/components/ui/ReusableModal.vue';
import type { TaskRequest } from '@/types/interfaces/task';

const props = defineProps<{
  columnId: number;
  title: string;
}>();

const authStore = useAuthStore();
const projectStore = useProjectStore();
const showTaskModal = ref(false);

const columnTasks = computed(() => {
    const column = projectStore.projectState?.boardColumns.find(
        (col) => col.id === props.columnId
    );
    return column ? column.tasks : [];
});

const toggleTaskModal = () => {
    showTaskModal.value = !showTaskModal.value;
};

const handleTaskAdded = async (newTask: TaskRequest) => {
    try {
        if (!authStore.user?.id) {
            throw new Error('Utilisateur non authentifié');
        } else if (!authStore.token) {
            throw new Error('Token non fourni');
        }

        await projectStore.addTask(props.columnId, newTask, authStore.token);

        showTaskModal.value = false
    } catch (error) {
        console.error("Erreur lors de l'ajout de la tâche :", error);
    }
};
</script>

<template>
    <div class="bg-slate-400 md:w-1/4 mb-2 rounded-md p-4">
        <h3 class="text-xl font-bold text-white">{{ title }}</h3>

        <div v-if="columnTasks.length > 0" class="mt-4 space-y-4">
            <TaskCard
                v-for="task in columnTasks"
                :key="task.id"
                :column-id="columnId"
                :task="task"
            />
        </div>
        <div v-else class="text-white text-center mt-4">
            Aucune tâche disponible.
        </div>

        <button
            class="block mt-4 mx-auto bg-blue-500 text-white rounded-md px-4 py-2"
            @click="toggleTaskModal"
        >
            Ajouter une tâche
        </button>

        <ReusableModal v-model="showTaskModal">
            <TaskForm
                :column-id="columnId"
                @close="toggleTaskModal"
                @submit="handleTaskAdded"
            />
        </ReusableModal>
    </div>
</template>
