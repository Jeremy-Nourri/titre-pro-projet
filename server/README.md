# API de l'application Project Flow

Cette API RESTful permet de gérer des projets, des colonnes de tableau Kanban, des tâches et des utilisateurs. Elle est construite avec **Spring Boot** et expose une documentation OpenAPI via Swagger.

## Table des matières

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Documentation API](#documentation-api)
- [Endpoints principaux](#endpoints-principaux)


---

## Introduction

Cette API offre un ensemble complet de fonctionnalités pour créer et gérer des projets, des tâches, et des utilisateurs avec des rôles (admin ou membre). Elle inclut l'authentification sécurisée des utilisateurs.

---

## Technologies

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Security**
- **JWT** pour l'authentification.
- **Spring Data JPA** avec support MySQL.
- **OpenAPI/Swagger** pour la documentation.

---

## Fonctionnalités

- **Gestion des projets** : Création, mise à jour, suppression, ajout d'utilisateurs.
- **Colonnes Kanban** : Ajout, modification et gestion des colonnes d'un projet.
- **Tâches** : Création, modification, déplacement entre colonnes.
- **Utilisateurs** : Enregistrement, gestion des rôles, connexion et déconnexion sécurisées.

---

## Prérequis

- Java 17 ou version supérieure
- Maven 3.6+ pour la gestion des dépendances
- Une base de données MySQL (ou H2 pour les tests)

---

## Installation

1. **Clonez le projet** :
   ```bash
   git clone https://github.com/Jeremy-Nourri/titre-pro-projet.git
   cd titre-pro-projet/server
   ```

2. **Installez les dépendances** :
   ```bash
   mvn clean install
   ```

3. **Configurez la base de données** :
   Modifiez le fichier `application.properties` dans `src/main/resources` pour y insérer vos informations de base de données :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ma_base
   spring.datasource.username=utilisateur
   spring.datasource.password=motdepasse
   ```

4. **Lancez l'application** :
   ```bash
   mvn spring-boot:run
   ```

---

## Configuration

### Base de données
Le projet est configuré pour utiliser MySQL, mais H2 est disponible pour les tests.

### Ports par défaut
L'API est exposée par défaut sur `http://localhost:8080`.

### Authentification
L'API utilise JWT pour sécuriser les endpoints. Les tokens sont générés après une connexion réussie.

---

## Utilisation

### Swagger UI
Une fois l'application démarrée, accédez à la documentation interactive via Swagger :
- **Swagger UI** : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Documentation API

### Endpoints principaux

#### Gestion des projets
- **Créer un projet** :  
  `POST /api/projects/create`  
  Corps requis :
  ```json
  {
    "name": "Nom du projet",
    "description": "Description du projet",
    "startDate": "2023-01-01",
    "endDate": "2023-12-31",
    "createdBy": 1
  }
  ```

- **Récupérer un projet** :  
  `GET /api/projects/{projectId}`

- **Modifier un projet** :  
  `PUT /api/projects/{projectId}`

- **Supprimer un projet** :  
  `DELETE /api/projects/{projectId}`

#### Gestion des colonnes
- **Créer une colonne** :  
  `POST /api/projects/{projectId}/columns`

- **Récupérer une colonne** :  
  `GET /api/projects/{projectId}/columns/{columnId}`

#### Gestion des tâches
- **Créer une tâche** :  
  `POST /api/projects/{projectId}/columns/{boardColumnId}/tasks`  
  Corps requis :
  ```json
  {
    "title": "Nom de la tâche",
    "priority": "HIGH",
    "taskStatus": "NOT_STARTED",
    "boardColumnId": 1,
    "dueDate": "2023-05-15"
  }
  ```

- **Récupérer une tâche** :  
  `GET /api/projects/{projectId}/columns/{boardColumnId}/tasks/{taskId}`

- **Déplacer une tâche** :  
  `PUT /api/projects/{projectId}/columns/{boardColumnId}/tasks/{taskId}/move`

#### Gestion des utilisateurs
- **Créer un utilisateur** :  
  `POST /api/users/register`  
  Corps requis :
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "password",
    "position": "Developer"
  }
  ```

- **Connexion** :  
  `POST /api/login`  
  Corps requis :
  ```json
  {
    "email": "john.doe@example.com",
    "password": "password"
  }
  ```

- **Déconnexion** :  
  `POST /api/logout`  
  En-tête requis :  
  `Authorization: Bearer {token}`

---


