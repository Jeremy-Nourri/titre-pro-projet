<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
  
const props = defineProps({
    message: { type: String, required: true },
    duration: { type: Number, default: 3000 },
    type: { type: String, default: 'info' },
});
  
const isVisible = ref(false);
  
let timeoutId: number | NodeJS.Timeout;
  
onMounted(() => {
    isVisible.value = true;
  
    timeoutId = setTimeout(() => {
	  isVisible.value = false;
    }, props.duration);
});
  
onBeforeUnmount(() => {
    clearTimeout(timeoutId);
});
  
const closeToast = () => {
    isVisible.value = false;
};
</script>

<template>
    <div
        v-if="isVisible"
        class="fixed inset-0 flex items-center justify-center"
    >
        <div
            class="bg-white border border-gray-200 shadow-lg rounded-lg p-4 max-w-sm w-full text-center transform transition-all duration-300 ease-in-out scale-90 opacity-0"
            :class="{ 'opacity-100 scale-100': isVisible }"
        >
            <div class="flex items-center space-x-3">
                <div>
                    <svg
                        v-if="props.type === 'success'"
                        class="w-6 h-6 text-green-500"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M5 13l4 4L19 7"
                        />
                    </svg>
                    <svg
                        v-else-if="props.type === 'error'"
                        class="w-6 h-6 text-red-500"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M6 18L18 6M6 6l12 12"
                        />
                    </svg>
                    <svg
                        v-else
                        class="w-6 h-6 text-gray-500"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M12 8v4m0 4h.01M12 12a4 4 0 110-8 4 4 0 010 8z"
                        />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-black">{{ props.message }}</p>
                </div>
            </div>
            <button
                class="absolute top-2 right-2 text-gray-400 hover:text-gray-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-400"
                @click="closeToast"
            >
                <span class="sr-only">Fermer</span>
                <svg
                    class="w-5 h-5"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                >
                    <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M6 18L18 6M6 6l12 12"
                    />
                </svg>
            </button>
        </div>
    </div>
</template>
