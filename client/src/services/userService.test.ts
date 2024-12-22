import { describe, it, expect, vi } from 'vitest';
import { api, handleApiError } from '@/api';
import { createUser } from '@/services/userService';
import { type UserRequest, type UserResponse, Position } from '@/types/interfaces/user';

vi.mock('@/api', () => ({
    api: {
        post: vi.fn(),
    },
    handleApiError: vi.fn(() => "Erreur simulée"),
}));

describe('User Service', () => {
    it('should create a user successfully', async () => {
        const mockUser: UserRequest = {
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@example.com',
            password: 'password123',
            position: Position.DEVELOPER
        };

        const mockResponse: UserResponse = {
            id: 1,
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@example.com',
            position: Position.DEVELOPER,
            createdAt: '2024-01-01T00:00:00Z',
            updatedAt: '2024-01-01T00:00:00Z',
            createdProjects: [],
            userProjects: [],
        };

        (api.post as ReturnType<typeof vi.fn>).mockResolvedValueOnce({ data: mockResponse });
        const result = await createUser(mockUser);

        expect(api.post).toHaveBeenCalledWith('/users', mockUser);
        expect(result).toEqual(mockResponse);
        expect(vi.mocked(handleApiError)).not.toHaveBeenCalled();
    });

    it('should return an error message on failure', async () => {
        const mockError = {
            response: {
                status: 400,
                data: { message: 'Invalid request' },
            },
        };

        (api.post as ReturnType<typeof vi.fn>).mockRejectedValueOnce(mockError);

        const result = await createUser({
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            position: '',
        });

        expect(result).toBe("Erreur simulée");
        expect(vi.mocked(handleApiError)).toHaveBeenCalledWith(mockError);
    });
});
