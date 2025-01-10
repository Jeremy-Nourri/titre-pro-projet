<script setup lang="ts">
import { ref, computed } from 'vue';
import { useProjectStore } from '@/stores/projectStore';
import TaskCard from '@/components/TaskCard.vue';
import TaskForm from '@/components/TaskForm.vue';
import ReusableModal from '@/components/ui/ReusableModal.vue';
import type { TaskRequest } from '@/types/interfaces/task';
import { PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline';


const priorityOrder = {
    HIGH: 1,
    MEDIUM: 2,
    LOW: 3,
};

const props = defineProps<{
  columnId: number;
  title: string;
}>();

const projectStore = useProjectStore();

const isEditing = ref(false);
const newTitle = ref(props.title);
const showDeleteModal = ref(false);
const showTaskModal = ref(false);
const sortOption = ref<string>('priority');

const columnTasks = computed(() => {
    const column = projectStore.projectState?.boardColumns.find(
        (col) => col.id === props.columnId
    );
    return column ? column.tasks : [];
});


const sortedTasks = computed(() => {
    if (sortOption.value === 'priority') {
        return columnTasks.value.slice().sort((a, b) => {
            return priorityOrder[a.priority] - priorityOrder[b.priority];
        });
    } else if (sortOption.value === 'alphabetical') {
        return columnTasks.value.slice().sort((a, b) => {
            return a.title.localeCompare(b.title, undefined, { sensitivity: 'base' });
        });
    }
    return columnTasks.value;
});

const toggleTaskModal = () => {
    showTaskModal.value = !showTaskModal.value;
};

const onSubmitTask = async (task: TaskRequest) => {
    try {
        if (task.id) {
            await projectStore.updateTaskDetails(props.columnId, task.id, task);
        } else {

            await projectStore.addTask(task.columnBoardId, task);
        }
    } catch (err) {
        console.error('Erreur :', err);
    }
};

const toggleEdit = () => {
    isEditing.value = !isEditing.value;
    newTitle.value = props.title;
};

const saveTitle = async () => {
    try {
        await projectStore.updateColumnName(props.columnId, newTitle.value);
        isEditing.value = false;
    } catch (error) {
        console.error('Erreur lors de la mise à jour du titre:', error);
    }
};

const confirmDelete = async () => {
    try {
        await projectStore.removeColumn(props.columnId);
        showDeleteModal.value = false;
    } catch (error) {
        console.error('Erreur lors de la suppression de la colonne:', error);
    }
};

</script>


<template>
    <div class="bg-slate-200 md:w-[35%] mb-2 rounded-md md:p-3 p-2 relative">
        <div v-if="!isEditing" class="flex justify-end gap-4 mt-1 mb-1 mr-1">
            <button
                class="group justify-center rounded-sm hover:white outline outline-4 outline-bluecolor
                 bg-bluecolor hover:bg-white ease-in duration-300 px-0.5 py-0.5"
                title="Modifier le titre"
                @click="toggleEdit"
            >
                <PencilSquareIcon class="size-4 text-white group-hover:text-bluecolor ease-in duration-300" />
            </button>
            <button
                class="group justify-center rounded-sm hover:text-bluecolor outline outline-4 outline-bluecolor 
                bg-bluecolor hover:bg-white ease-in duration-300 px-0.5 py-0.5"
                title="Supprimer la colonne"
                @click="showDeleteModal = true"
            >
                <TrashIcon class="size-4 text-white group-hover:text-bluecolor ease-in duration-300" />
            </button>
        </div>
  
        <div class="flex justify-between items-center">
            <div v-if="!isEditing">
                <h4 class="text-gray-900">
                    {{ title }}
                </h4>
            </div>
            <div v-else class="w-full">
                <input
                    v-model="newTitle"
                    class="form-input"
                    placeholder="Nouveau titre"
                />
                <div class="flex justify-center gap-4 mt-2">
                    <button class="bg-green-500 text-white text-xs px-2 py-1 rounded" @click="saveTitle">
                        Enregistrer
                    </button>
                    <button class="bg-gray-500 text-white text-xs px-2 py-1 rounded" @click="toggleEdit">
                        Annuler
                    </button>
                </div>
            </div>
        </div>
  
        <div class="mt-4">
            <label for="sort" class="p-text-small block text-gray-900 mb-1">Trier par :</label>
            <select
                id="sort"
                v-model="sortOption"
                class="w-full rounded-md p-text-small bg-white px-2 py-1 text-gray-900 hover:cursor-pointer"
            >
                <option value="priority">Priorité</option>
                <option value="alphabetical">Alphabétique</option>
            </select>
        </div>
  
        <div v-if="sortedTasks.length > 0" class="mt-4 space-y-4">
            <TaskCard
                v-for="task in sortedTasks"
                :key="task.id"
                :column-id="columnId"
                :task="task"
            />
        </div>
        <div v-else class="p-text text-gray-900 text-center mt-4">
            Aucune tâche disponible
        </div>
  
        <button
            class="button-primary mt-4"
            @click="toggleTaskModal"
        >
            Ajouter une tâche
        </button>
  
        <ReusableModal v-model="showTaskModal">
            <TaskForm
                :column-id="columnId"
                @close="toggleTaskModal"
                @submit="onSubmitTask"
            />
        </ReusableModal>
  
        <ReusableModal v-model="showDeleteModal">
            <div class="p-4 text-center">
                <p class="p-text mb-4">Voulez-vous vraiment supprimer cette colonne ?</p>
                <button
                    class="button-base bg-red-500 text-white mr-2"
                    @click="confirmDelete"
                >
                    Supprimer
                </button>
                <button
                    class="button-base bg-gray-500 text-white rounded-md"
                    @click="showDeleteModal = false"
                >
                    Annuler
                </button>
            </div>
        </ReusableModal>
    </div>
</template>
  