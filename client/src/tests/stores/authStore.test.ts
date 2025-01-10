import { describe, it, expect, beforeEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore } from '@/stores/authStore';
import * as authService from '@/services/authService';
import * as userService from '@/services/userService';
import { Position } from '@/types/interfaces/user';

vi.mock('vue-router', () => ({
    useRouter: vi.fn(() => ({
        currentRoute: { value: { query: {} } },
        push: vi.fn(),
    })),
}));

vi.mock("jwt-decode", () => {
    return {
        jwtDecode: vi.fn((token) => {
            if (token === "INVALID_TOKEN") {
                throw new Error("Invalid token");
            }
            return { userId: 1, sub: "john.doe@email.com", exp: 1800000000 }; 
        }),
    };
});
  
vi.mock('@/services/authService', () => ({
    login: vi.fn(),
    logout: vi.fn(),
}));

vi.mock('@/services/userService', () => ({
    getUserDetails: vi.fn(),
    createUser: vi.fn(),
}));

describe('authStore tests', () => {

    const fakeToken =
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEwMCwic3ViIjoiam9obkBkYWUuY29tIiwiZXhwIjoxODAwMDAwMDAwfQ.sE8BX4Nl7a0Fh8yFwnGH-sFG0IB0Nl7Xme9UGC-sNzU';

    const fakeUser = {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@doe.com',
        token: 'FAKE_TOKEN_EXAMPLE',
        createdProjects: [],
        userProjects: [],
    };

    let authStore: ReturnType<typeof useAuthStore>;

    beforeEach(() => {
        setActivePinia(createPinia());
        authStore = useAuthStore(); 
        vi.clearAllMocks();
    });

    it('Devrait avoir un état initial correct', () => {
    
        expect(authStore.token).toBeNull();
        expect(authStore.user).toBeNull();
        expect(authStore.isLoading).toBe(false);
        expect(authStore.error).toBeNull();
    });

    it('signin - succès', async () => {
        vi.spyOn(authService, 'login').mockResolvedValue(fakeUser);

        await authStore.signin({ email: 'john@doe.com', password: '123456' });

        expect(authStore.token).toBe(fakeUser.token);
        expect(authStore.user).toEqual(fakeUser);
        expect(authStore.error).toBeNull();
        expect(authStore.isLoading).toBe(false);
    });

    it('signin - erreur', async () => {
        vi.spyOn(authService, 'login').mockRejectedValue(new Error('Email ou mot de passe incorrect'));

        await authStore.signin({ email: 'john@doe.com', password: 'wrongpassword' });

        expect(authStore.token).toBeNull();
        expect(authStore.user).toBeNull();
        expect(authStore.error).not.toBeNull();
        expect(authStore.isLoading).toBe(false);
    });

    it('signout - réinitialise le token et l’utilisateur', async () => {
        vi.spyOn(authService, 'logout').mockResolvedValue('Déconnexion réussie');

        authStore.token = fakeToken;
        authStore.user = fakeUser;

        await authStore.signout();

        expect(authStore.token).toBeNull();
        expect(authStore.user).toBeNull();
        expect(authStore.isLoading).toBe(false);
        expect(authStore.error).toBeNull();
    });

    it('fetchUser - succès', async () => {
        authStore.token = fakeToken;

        const fetchedUser = {
            id: 1,
            firstName: 'John',
            lastName: 'Doe',
            email: 'john@doe.com',
            createdProjects: [],
            userProjects: [],
            position: Position.DEVELOPER,
            createdAt: '',
        };
        vi.spyOn(userService, 'getUserDetails').mockResolvedValue(fetchedUser);

        const decodeTokenSpy = vi.spyOn(authStore, 'decodeToken');

        await authStore.fetchUser();

        expect(userService.getUserDetails).toHaveBeenCalledWith(1);

        expect(authStore.user).toEqual(fetchedUser);
        expect(authStore.error).toBeNull();
        expect(authStore.isLoading).toBe(false);
    });

    
});
