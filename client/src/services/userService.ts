import { api, handleApiError, fetchWithAuth } from '@/api';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';


export const createUser = async (user: UserRequest): Promise<UserResponse | string> => {
    try {
        const response = await api.post('users/register', user);
        return response.data;
    } catch (error) {
        return handleApiError(error);
    }
};

export const getUserDetails = async (userId: number, token: string): Promise<UserResponse | string> => {
    return await fetchWithAuth<UserResponse>('GET', `/users/${userId}`, token);
};