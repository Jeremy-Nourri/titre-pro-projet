import { defineStore } from 'pinia';
import { ref } from 'vue';
import { createUser } from '@/services/userService';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';

export const useUserStore = defineStore('user', () => {
	
  const users = ref<UserResponse[]>([]);
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  const addUser = async (user: UserRequest) => {
    isLoading.value = true;
    error.value = null;

    const result = await createUser(user);
    if (typeof result === 'string') {
      error.value = result;
    } else {
      users.value.push(result);
    }

    isLoading.value = false;
  };

  return {
    users,
    isLoading,
    error,
    addUser,
  };
});
