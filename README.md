# Documentation pour le déploiement de l'application project Flow

## Pré-requis

1. **Outils requis** :
   - Docker installé sur votre machine.
   - GitLab pour l'intégration et le déploiement continu.

2. **Variables d'environnement nécessaires** :
   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` : Informations pour la connexion à la base de données.
   - `JWT_SECRET`, `JWT_EXPIRATION` : Paramètres pour l'authentification JWT.
   - `VITE_API_URL` : URL de l'API utilisée par le frontend.

## Structure des fichiers

- **`.gitlab-ci.yml`** : Configuration CI/CD pour GitLab.
- **`docker-compose.yml`** : Définition des services nécessaires au déploiement.
- **`Dockerfile`** (client et server) : Instructions pour créer les images Docker.

---

## 1. Pipeline CI/CD sur GitLab

### Étapes du Pipeline

1. **Test** :
   - Les tests du backend sont effectués avec JUnit et Mockito, ils sont exécutés par Maven.
   - Les tests du frontend sont effectués avec ViTest et lancés avec npm via un script.

2. **Build** :
   - Le backend est packagé en un fichier `.jar`.
   - Le frontend est construit et les fichiers statiques sont générés.

3. **Docker_Build**
- Construction des images Docker pour le backend et le frontend.
- Les images sont taguées et poussées vers le registre Docker de GitLab.

3. **Déploiement** :
   - Les services Docker sont démarrés avec Docker Compose.

### Fichier `.gitlab-ci.yml`

Voici un aperçu de la configuration :
- **Images utilisées** : `maven`, `node`, `docker`.
- **Variables définies** pour configurer l'application.
- **Artefacts générés** :
  - Backend : `server/target/*.jar`
  - Frontend : `client/dist`

---

## 2. Définition des Services avec Docker Compose

### Fichier `docker-compose.yml`

- **Backend (`server`)** :
  - Utilise le Dockerfile du dossier `server`.
  - Expose le port `8080`.
  - Communique avec le frontend via un réseau Docker (`app-network`).

- **Frontend (`client`)** :
  - Utilise le Dockerfile du dossier `client`.
  - Expose le port `3000`.

### Commandes essentielles

1. Construire les services :
   ```bash
   docker-compose build
   ```

2. Démarrer les services en arrière-plan :
   ```bash
   docker-compose up -d
   ```

3. Arrêter les services :
   ```bash
   docker-compose down
   ```

---

## 3. Définition des Images Docker

### Backend (`server/Dockerfile`)

Assurez-vous que votre Dockerfile inclut les étapes suivantes :
- Installation de Java pour exécuter l'application Spring Boot.
- Copie du fichier `.jar` généré dans l'image.

### Frontend (`client/Dockerfile`)

- Utilisation d'une image Node.js pour construire les fichiers statiques.
- Serveur web léger (Nginx) pour servir les fichiers générés.

---

## 4. Déploiement

### Depuis GitLab CI/CD

1. Assurez-vous que les variables d'environnement sont configurées dans les **paramètres du projet GitLab**.
2. Poussez les modifications sur la branche `main` :
   ```bash
   git push origin main
   ```
3. Le pipeline CI/CD exécutera automatiquement les étapes définies.

### Déploiement manuel

1. Lancez Docker Compose :
   ```bash
   docker-compose up -d
   ```
2. Vérifiez l'état des conteneurs :
   ```bash
   docker ps -a
   ```

---

## 5. Résolution des problèmes

1. **Erreurs liées aux variables d'environnement** :
   - Vérifiez que toutes les variables nécessaires sont définies dans GitLab et localement pour Docker Compose.

2. **Services qui ne démarrent pas** :
   - Inspectez les logs des conteneurs :
     ```bash
     docker logs <container_id>
     ```

3. **Problèmes de connectivité entre services** :
   - Vérifiez que le réseau Docker est bien configuré (`app-network`).
