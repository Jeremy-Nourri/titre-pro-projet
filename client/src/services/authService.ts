import { api } from '@/api';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
    try {
        const response = await api.post<LoginResponse>('/login', credentials);
        return response.data;

    } catch (error) {
        console.error(error);
        throw error;
    }
    
};

export const logout = async (): Promise<string> => {
    try {
        const response = await api.post('/logout');
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }

};
