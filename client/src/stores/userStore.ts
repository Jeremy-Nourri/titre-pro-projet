import { defineStore } from 'pinia';
import { ref } from 'vue';
import { createUser } from '@/services/userService';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';

export const useUserStore = defineStore('user', () => {
	
    const users = ref<UserResponse[]>([]);
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const resetError = () => {
        error.value = null;
    };

    const addUser = async (user: UserRequest) => {
        isLoading.value = true;
        resetError();

        const response = await createUser(user);

        if (typeof response === 'string') {
            error.value = response;

        } else {
            users.value.push(response);
        }
        
        isLoading.value = false;
    };

    return {
        users,
        isLoading,
        error,
        addUser,
        resetError
    };
});
