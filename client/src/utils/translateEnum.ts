export const priorityTranslations: Record<'LOW' | 'MEDIUM' | 'HIGH', string> = {
    LOW: 'faible',
    MEDIUM: 'moyenne',
    HIGH: 'élevée',
};
  
export const taskStatusTranslations: Record<'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED', string> = {
    NOT_STARTED: 'non commencé',
    IN_PROGRESS: 'en cours',
    COMPLETED: 'terminé',
};
  
export const translateEnum = <T extends string>(value: T, translations: Record<T, string>): string => {
    return translations[value]?.toLowerCase() || value.toLowerCase();
};
  