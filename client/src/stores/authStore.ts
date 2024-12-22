import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';
import { login } from '@/services/authService';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore("authStore", () => {

    const token = ref<string | null>(null)
    const user = ref<LoginResponse | null>(null)
    const isLoading = ref(false);
    const error = ref<string | null>(null);

    const router = useRouter();

    const signin = async (credentials: LoginRequest) => {
        isLoading.value = true;
        error.value = null;
    
        try {
            const response = await login(credentials);
    
            if (typeof response === 'string') {
                error.value = response;
            } else {
                user.value = response;
                localStorage.setItem("user", JSON.stringify(response.token));
    
                await router.push({ name: 'DashboardView' });
            }
        } catch (err) {
            console.error("Error during navigation:", err);
            error.value = "Une erreur s'est produite lors de la redirection.";
        } finally {
            isLoading.value = false;
        }
    };
    

    return {
        token,
        user,
        isLoading,
        error,
        signin
    }
})