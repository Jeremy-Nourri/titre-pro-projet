import { defineStore } from 'pinia';
import { ref } from 'vue';
import { jwtDecode } from 'jwt-decode';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';
import { getUserDetails, login } from '@/services/authService';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore("authStore", () => {
    const token = ref<string | null>(localStorage.getItem("token"));
    const user = ref<Partial<LoginResponse | null>>(null);
    const isLoading = ref(false);
    const error = ref<string | null>(null);
    const router = useRouter();

    const resetError = () => {
        error.value = null;
    };

    const updateAuthData = (response: LoginResponse) => {
        token.value = response.token;
        localStorage.setItem("token", response.token);
        user.value = {
            id: response.id,
            firstName: response.firstName,
            lastName: response.lastName,
            email: response.email,
            position: response.position,
            createdAt: response.createdAt,
            updatedAt: response.updatedAt,
            createdProjects: response.createdProjects,
            userProjects: response.userProjects,
        };
    };

    const decodeToken = (token: string): { userId: number; sub: string; exp: number } | null => {
        try {
            return jwtDecode(token);
        } catch (err) {
            console.error("Échec du décodage du token :", err);
            return null;
        }
    };

    const checkTokenInStore = () => {
        if (!user.value) {
            signout();
            throw new Error('Utilisateur non authentifié');
        } else if (!token.value) {
            signout();
            throw new Error('Token non fourni');
        } else {
            const decodedToken = decodeToken(token.value);
            if (!decodedToken || isTokenExpired(decodedToken.exp)) {
                signout();
                throw new Error('Token expiré');
            }
        }
    }

    const isTokenExpired = (exp: number): boolean => {
        const now = Math.floor(Date.now() / 1000);
        return exp < now;
    };

    const signin = async (credentials: LoginRequest) => {
        isLoading.value = true;
        resetError();
        try {
            const response = await login(credentials);
            if (typeof response === 'string') {
                error.value = response;
            } else {
                updateAuthData(response);
                await router.push({ name: 'DashboardView' });
            }
        } catch (err) {
            console.error("Erreur lors de la connexion :", err);
        } finally {
            isLoading.value = false;
        }
    };

    const signout = async () => {
        token.value = null;
        user.value = null;
        localStorage.removeItem("token");
        await router.push({ name: 'HomeView' });
    };

    const fetchUser = async (tokenParam: string) => {
        isLoading.value = true;
        resetError();
        try {
            const decoded = decodeToken(tokenParam);

            if (decoded) {
                const response = await getUserDetails(decoded.userId, tokenParam);
                if (typeof response === 'string') {
                    error.value = response;
                } else {
                    updateAuthData(response);
                }
            } else {
                throw new Error("Impossible de décoder le token.");
            }
        } catch (err) {
            console.error("Erreur lors de la récupération des informations de l'utilisateur :", err);
        } finally {
            isLoading.value = false;
        }

    };

    const initializeAuth = async () => {
        const storedToken = localStorage.getItem("token");
        if (storedToken) {
            const decoded = decodeToken(storedToken);
            if (decoded && !isTokenExpired(decoded.exp)) {
                token.value = storedToken;
    
                if (!user.value || !user.value.id) {
                    await fetchUser(storedToken);
                }
            } else {
                await signout();
            }
        } else {
            await signout();
        }
    };

    return {
        token,
        user,
        isLoading,
        error,
        signin,
        signout,
        initializeAuth,
        fetchUser,
        checkTokenInStore,
        resetError
    };
});
