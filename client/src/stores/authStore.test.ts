import { describe, it, expect, vi, beforeEach } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore } from './authStore';
import { login } from '@/services/authService';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';

vi.mock('@/services/authService', () => ({
    login: vi.fn(),
}));

describe('authStore', () => {
    let authStore: ReturnType<typeof useAuthStore>;

    const mockCredentials: LoginRequest = {
        email: 'test@example.com',
        password: 'password123',
    };

    const mockResponse: LoginResponse = {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'test@example.com',
        position: 'Developer',
        createdAt: '2023-01-01T00:00:00Z',
        updatedAt: '2023-01-01T00:00:00Z',
        token: 'mock-token',
    };

    beforeEach(() => {
        setActivePinia(createPinia());
        authStore = useAuthStore();
        localStorage.clear();
    });

    it('should handle successful login', async () => {

        (login as ReturnType<typeof vi.fn>).mockResolvedValue(mockResponse);

        await authStore.signin(mockCredentials);

        expect(authStore.isLoading).toBe(false);
        expect(authStore.error).toBe(null);
        expect(authStore.user).toEqual(mockResponse);
        expect(localStorage.getItem('user')).toBe(JSON.stringify(mockResponse.token));
    });

    it('should handle failed login', async () => {

        const mockError = 'Invalid credentials';
        (login as ReturnType<typeof vi.fn>).mockResolvedValue(mockError);

        await authStore.signin(mockCredentials);

        expect(authStore.isLoading).toBe(false);
        expect(authStore.error).toBe(mockError);
        expect(authStore.user).toBe(null);
        expect(localStorage.getItem('user')).toBe(null);
    });

    it('should set loading state during login', async () => {

        (login as ReturnType<typeof vi.fn>).mockImplementation(
            () => new Promise((resolve) => {
                setTimeout(() => resolve(mockResponse), 1000);
            })
        );

        const signinPromise = authStore.signin(mockCredentials);

        expect(authStore.isLoading).toBe(true);

        await signinPromise;
    });
});
