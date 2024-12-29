import axios, { AxiosError} from "axios";

export const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000,
});

export const handleApiError = (error: unknown): string => {
    if (error instanceof AxiosError) {
        if (error.response) {
            switch (error.response.status) {
            case 400:
                return "Requête invalide. Veuillez vérifier les données saisies.";
            case 401:
                return "Non autorisé. Veuillez vérifier les données saisies.";
            case 403:
                return "Accès refusé. Vous n'avez pas les permissions nécessaires.";
            case 404:
                return "Ressource introuvable. Veuillez réessayer.";
            case 500:
                return "Erreur interne du serveur. Veuillez réessayer plus tard.";
            default:
                return `${error.response.status}`;
            }
        }

        if (error.request) {
            return "Le serveur n'a pas répondu. Veuillez vérifier votre connexion internet.";
        }
    }

    return "Une erreur inattendue s'est produite. Veuillez réessayer.";
};

export const fetchWithAuth = async <T>(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE',
    url: string,
    token: string,
    data?: object | string
): Promise<T> => {
    try {
        const response = await api.request<T>({
            method,
            url,
            data,
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw new Error(handleApiError(error));
    }
};
