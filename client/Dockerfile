# Étape 1 : Construction de l'application
FROM node:18 AS builder

WORKDIR /app

# Copier les fichiers de configuration et installer les dépendances
COPY package.json package-lock.json ./
RUN npm ci

# Copier le reste des fichiers et construire l'application
COPY . .
RUN npm run build

# Étape 2 : Servir l'application avec Nginx
FROM nginx:alpine

# Copier les fichiers construits dans le répertoire par défaut de Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Exposer le port 80
EXPOSE 80

# Commande de démarrage de Nginx
CMD ["nginx", "-g", "daemon off;"]
