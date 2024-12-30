import { defineStore } from "pinia";
import { ref } from "vue";
import { jwtDecode } from "jwt-decode";
import type { LoginRequest, LoginResponse } from "@/types/interfaces/login";
import { login } from "@/services/authService";
import { createUser, getUserDetails } from "@/services/userService";
import { useRouter } from "vue-router";
import type { UserRequest } from "@/types/interfaces/user";

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
        user.value = response;
    };

    const decodeToken = (token: string): { userId: number; sub: string; exp: number } | null => {
        try {
            return jwtDecode(token);
        } catch (err) {
            console.error("Échec du décodage du token :", err);
            return null;
        }
    };

    const isTokenExpired = (exp: number): boolean => {
        const now = Math.floor(Date.now() / 1000);
        return exp < now;
    };

    const isAuthenticated = (): boolean => {
        if (!token.value) return false;
        const decoded = decodeToken(token.value);
        return decoded !== null && !isTokenExpired(decoded.exp);
    };

    const handleError = (err: unknown): string => {
        if (err instanceof Error) {
            return err.message;
        }
        return "Une erreur inattendue s'est produite.";
    };

    const signin = async (credentials: LoginRequest) => {
        isLoading.value = true;
        resetError();
        try {
            const response = await login(credentials);
            updateAuthData(response);

            const redirectPath = router.currentRoute.value.query.redirect as string;

            if (redirectPath) {
                await router.push(redirectPath);
            } else {
                await router.push({ name: "DashboardView" });
            }
        } catch (err) {
            console.error("Erreur lors de la connexion :", err);
            error.value = handleError(err);
        } finally {
            isLoading.value = false;
        }
    };

    const signout = async () => {
        token.value = null;
        user.value = null;
        localStorage.removeItem("token");
        await router.push({ name: "HomeView" });
    };

    const fetchUser = async () => {
        if (!token.value) return;
        isLoading.value = true;
        resetError();
        try {
            const decoded = decodeToken(token.value);
            if (decoded) {
                const response = await getUserDetails(decoded.userId);
                user.value = response;
            } else {
                throw new Error("Impossible de décoder le token.");
            }
        } catch (err) {
            console.error("Erreur lors de la récupération des informations de l'utilisateur :", err);
            error.value = handleError(err);
        } finally {
            isLoading.value = false;
        }
    };

    const addUser = async (userRequest: UserRequest) => {
        isLoading.value = true;
        resetError();
        try {
            await createUser(userRequest);
        } catch (err) {
            error.value = handleError(err);
        } finally {
            isLoading.value = false;
        }
    };
    
    const getUserById = async (userId: number) => {
        isLoading.value = true;
        resetError();
        try {
            const response = await getUserDetails(userId);
            if (response) {
                user.value = response;
            }
        } catch (err) {
            error.value = handleError(err);
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
                if (!user.value) {
                    await fetchUser();
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
        getUserById,
        addUser,
        isAuthenticated,
        resetError,
    };
});
