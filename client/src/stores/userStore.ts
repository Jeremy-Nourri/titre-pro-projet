import { defineStore } from 'pinia';
import { ref } from 'vue';
import { createUser, getUserDetails } from '@/services/userService';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';

export const useUserStore = defineStore('user', () => {
	
    const user = ref<UserResponse>();
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const resetError = () => {
        error.value = null;
    };

    const addUser = async (userRequest: UserRequest) => {
        isLoading.value = true;
        resetError();

        const response = await createUser(userRequest);

        if (typeof response === 'string') {
            error.value = response;

        } else {
            user.value = response;
        }
        
        isLoading.value = false;
    };

    const getUserById = async (userId: number, token: string) => {
        isLoading.value = true;
        resetError();

        const response = await getUserDetails(userId, token);

        if (typeof response === 'string') {
            error.value = response;

        } else {
            user.value = response;
        }
        
        isLoading.value = false;
    };

    return {
        user,
        isLoading,
        error,
        addUser,
        getUserById,
        resetError
    };
});
