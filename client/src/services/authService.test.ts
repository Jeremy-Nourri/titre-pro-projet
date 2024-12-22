import { describe, it, expect, vi } from 'vitest';
import { login } from './authService'; // Remplace par le chemin correct
import { api, handleApiError } from '@/api';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';

// Mock des dépendances
vi.mock('@/api', () => ({
    api: {
        post: vi.fn(),
    },
    handleApiError: vi.fn(),
}));

describe('login service', () => {
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

    it('should return login response data when API call is successful', async () => {

        (api.post as ReturnType<typeof vi.fn>).mockResolvedValue({ data: mockResponse });

        const result = await login(mockCredentials);

        expect(api.post).toHaveBeenCalledWith('login', mockCredentials);
        expect(result).toEqual(mockResponse);
    });

    it('should handle API errors and return error message', async () => {

        const mockError = new Error('Network Error');
        (api.post as ReturnType<typeof vi.fn>).mockRejectedValue(mockError);
        (handleApiError as ReturnType<typeof vi.fn>).mockReturnValue('An error occurred');

        const result = await login(mockCredentials);

        expect(api.post).toHaveBeenCalledWith('login', mockCredentials);
        expect(handleApiError).toHaveBeenCalledWith(mockError);
        expect(result).toBe('An error occurred');
    });
});
