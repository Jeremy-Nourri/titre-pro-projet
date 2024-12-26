import { api, handleApiError } from '@/api';

import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';

export const login = async (credentials: LoginRequest): Promise<LoginResponse | string> => {
    try {
        const response = await api.post('login',credentials);
        return response.data;
    } catch (error) {
        return handleApiError(error);
    }
};

export const getUserDetails = async (userId: number, token: string) => {

    try {
        const response = await api.get(`/users/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
        
    } catch (error) {
        localStorage.removeItem('token');
        return handleApiError(error);
    }
};



