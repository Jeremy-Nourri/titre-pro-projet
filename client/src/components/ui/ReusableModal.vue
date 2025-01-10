<script setup lang="ts">
import { ref, defineProps, watch  } from 'vue';
import { Dialog, DialogPanel, TransitionChild, TransitionRoot } from '@headlessui/vue';
  
const props = defineProps({
    modelValue: {
	  type: Boolean,
	  required: true
    }
});

const emit = defineEmits(['update:modelValue']);
  
const open = ref(props.modelValue);

watch(() => props.modelValue, (newValue) => {
    open.value = newValue;
});

const closeModal = () => {
    emit('update:modelValue', false);
};

</script>

<template>
    <TransitionRoot as="template" :show="open">
        <Dialog class="relative z-10" @close="closeModal">
            <TransitionChild
                as="template"
                enter="ease-out duration-300"
                enter-from="opacity-0"
                enter-to="opacity-100"
                leave="ease-in duration-200"
                leave-from="opacity-100"
                leave-to="opacity-0"
            >
                <div class="fixed inset-0 bg-gray-500/75 transition-opacity" />
            </TransitionChild>
  
            <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                    <TransitionChild
                        as="template"
                        enter="ease-out duration-300"
                        enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                        enter-to="opacity-100 translate-y-0 sm:scale-100"
                        leave="ease-in duration-200"
                        leave-from="opacity-100 translate-y-0 sm:scale-100"
                        leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                    >
                        <DialogPanel class="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all w-full sm:my-8 sm:w-full sm:max-w-lg">
                            <slot />
                        </DialogPanel>
                    </TransitionChild>
                </div>
            </div>
        </Dialog>
    </TransitionRoot>
</template>  