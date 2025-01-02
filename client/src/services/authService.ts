import { api } from '@/api';
import type { LoginRequest, LoginResponse } from '@/types/interfaces/login';

export const login = async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>('login', credentials);
    return response.data;
};

export const logout = async (): Promise<string> => {
    const response = await api.post('/logout');
    return response.data;
};
