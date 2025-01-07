import { AxiosError } from "axios";

type AppError = {
  message: string;
  status: number;
};
export function handleError(err: unknown): AppError {
    // 1) Si l'erreur est un objet du type { status, message } renvoyé par l'intercepteur
    if (typeof err === 'object' && err !== null && 'status' in err && 'message' in err) {
	  const typedError = err as { status: number; message: string };
	  return {
            status: typedError.status,
            message: typedError.message,
	  };
    }
  
    // 2) Si c’est une erreur Axios à l’état brut (si tu ne l’as pas transformée)
    if ((err as AxiosError).isAxiosError) {
	  const axiosErr = err as AxiosError<{ message?: string }>;
	  return {
            status: axiosErr.response?.status ?? 500,
            message: axiosErr.response?.data?.message ?? 'Une erreur est survenue',
	  };
    }
  
    // 3) Si c’est juste une erreur JS
    if (err instanceof Error) {
	  return {
            status: 500,
            message: err.message || 'Erreur inconnue',
	  };
    }
  
    // 4) Sinon, c’est inconnu
    return {
	  status: 500,
	  message: 'Erreur inconnue',
    };
}
  