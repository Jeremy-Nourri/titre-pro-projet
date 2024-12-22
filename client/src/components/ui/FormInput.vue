<script setup lang="ts">
import { defineProps, defineEmits, computed, ref } from 'vue';

const isFocused = ref(false);

const props = defineProps({
    modelValue: {
        type: String,
        default: '',
    },
    name: {
        type: String,
        default: '',
    },
    label: {
        type: String,
        default: '',
    },
    type: {
        type: String,
        default: 'text',
    },
    placeholder: {
        type: String,
        default: '',
    },
    autocomplete: {
        type: String,
        default: '',
    },
    error: {
        type: String,
        default: null,
    },
});

const emit = defineEmits(['update:modelValue']);

const internalValue = computed({
    get: () => props.modelValue,
    set: (value: string) => emit('update:modelValue', value),
});
</script>

<template>
    <div class="mt-4">
        <label :for="name" class="block mb-1 text-xs md:text-base font-medium">{{ label }}</label>
        <input
            v-model="internalValue"
            :name="name"
            :type="type"
            :placeholder="placeholder"
            :autocomplete="autocomplete"
            class="form-input"
            :style="{ outlineColor: props.error ? 'red' : isFocused ? '#4A90E2' : 'gray' }"
            required
            @focus="isFocused = true"
            @blur="isFocused = false"
        />
        <p v-if="error" class="text-danger lg:text-[10px] text-[9px] pl-1 mt-1">{{ error }}</p>
    </div>
</template>
<style scoped>

</style>