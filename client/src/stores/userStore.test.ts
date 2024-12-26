import { setActivePinia, createPinia } from 'pinia';
import { describe, it, beforeEach, expect, vi } from 'vitest';
import { useUserStore } from '@/stores/userStore';
import { createUser } from '@/services/userService';
import {
    type UserRequest,
    type UserResponse,
    Position,
} from '@/types/interfaces/user';

vi.mock('@/services/userService', () => ({
    createUser: vi.fn(),
}));

describe('User Store', () => {
    let userStore: ReturnType<typeof useUserStore>;

    beforeEach(() => {
        setActivePinia(createPinia());
        userStore = useUserStore();
    });

    it('should add a user successfully', async () => {
        const mockUser: UserRequest = {
            firstName: 'John',
            lastName: 'Doe',
            email: 'john.doe@example.com',
            password: 'password123',
            position: Position.DEVELOPER,
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

        (createUser as ReturnType<typeof vi.fn>).mockResolvedValueOnce(
            mockResponse
        );

        await userStore.addUser(mockUser);

        expect(createUser).toHaveBeenCalledWith(mockUser);
        expect(userStore.users).toContainEqual(mockResponse);
        expect(userStore.error).toBeNull();
        expect(userStore.isLoading).toBe(false);
    });

    it('should handle an error when adding a user', async () => {
        const mockError =
            'Requête invalide. Veuillez vérifier les données saisies.';

        (createUser as ReturnType<typeof vi.fn>).mockResolvedValueOnce(
            mockError
        );

        await userStore.addUser({
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            position: '',
        });

        expect(userStore.error).toBe(mockError);
        expect(userStore.users).toHaveLength(0);
        expect(userStore.isLoading).toBe(false);
    });
});
