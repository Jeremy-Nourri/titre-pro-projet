# Étape 1 : Construire l'application avec Node.js

# Utiliser l'image Node.js 18 comme image de base
FROM node:18 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration nécessaires
COPY package.json package-lock.json* ./

# Installer les dépendances
RUN npm ci

# Copier le reste des fichiers de l'application
COPY . .

# Construire l'application pour la production
RUN npm run build


# Étape 2 : Servir l'application avec Nginx
FROM nginx:alpine

# Copier les fichiers construits dans le répertoire par défaut de Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Exposer le port 80 pour le serveur
EXPOSE 80

# Commande de démarrage de Nginx
CMD ["nginx", "-g", "daemon off;"]
