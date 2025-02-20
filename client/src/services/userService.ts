import { api } from '@/api';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';

export const createUser = async (user: UserRequest): Promise<UserResponse> => {
    try {
        const response = await api.post<UserResponse>('/users/register', user);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const getUserDetails = async (userId: number): Promise<UserResponse> => {
    try {
        const response = await api.get<UserResponse>(`/users/${userId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};
