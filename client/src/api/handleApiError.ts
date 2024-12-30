import { AxiosError } from "axios";

interface ErrorDetails {
    timestamp: string;
    message: string;
    details: string;
}

export const handleApiError = (error: unknown): { userMessage: string; statusCode?: number } => {
    
    if (error instanceof AxiosError) {
		
        if (error.response) {
            const statusCode = error.response.status;
            let userMessage = "Une erreur s'est produite. Veuillez réessayer";

            const errorDetails = error.response.data as ErrorDetails;
            if (errorDetails?.message) {
                userMessage = errorDetails.message;
            }

            switch (statusCode) {
            case 400:
                userMessage = userMessage || "Requête invalide. Veuillez vérifier les données saisies";
                break;
            case 401:
                userMessage = userMessage || "Non autorisé. Veuillez vérifier vos identifiants";
                break;
            case 403:
                userMessage = userMessage || "Accès refusé. Vous n'avez pas les permissions nécessaires";
                break;
            case 404:
                userMessage = userMessage || "Ressource introuvable. Veuillez réessayer";
                break;
            case 409:
                userMessage = userMessage || "Conflit détecté. L'action ne peut pas être effectuée";
                break;
            case 500:
                userMessage = userMessage || "Erreur interne du serveur. Veuillez réessayer plus tard";
                break;
            default:
                userMessage = userMessage || `Erreur ${statusCode}. Veuillez contacter le support`;
            }

            return {
                userMessage,
                statusCode,
            };
        }

        if (error.request) {
            return {
                userMessage: "Le serveur n'a pas répondu. Veuillez vérifier votre connexion internet.",
            };
        }
    }

    return {
        userMessage: "Une erreur inattendue s'est produite. Veuillez réessayer.",
    };
};
