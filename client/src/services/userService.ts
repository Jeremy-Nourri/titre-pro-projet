import { api, handleApiError } from '@/api';
import type { UserRequest, UserResponse } from '@/types/interfaces/user';


export const createUser = async (user: UserRequest): Promise<UserResponse | string> => {
	try {
    const response = await api.post('/users', user);
    return response.data;
  } catch (error) {
    return handleApiError(error);
  }
};

