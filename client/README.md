# Client Vue.js pour l'application Project Flow

Ce projet est un client web développé en Vue.js, conçu pour interagir avec une API de gestion de projets. Il offre une interface utilisateur réactive et intuitive, facilitant la gestion des colonnes, des tâches et des utilisateurs.

## Table des matières

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Scripts disponibles](#scripts-disponibles)
- [Configuration](#configuration)
- [Utilisation](#utilisation)


---

## Introduction

Ce client Vue.js permet de gérer les projets en exploitant les fonctionnalités offertes par l'API backend. Grâce à cette interface, les utilisateurs peuvent :

- Créer, modifier et supprimer des projets.
- Créer, modifier et supprimer des colonnes.
- Créer, modifier et supprimer des tâches.
- Effectuer des actions d'authentification (connexion/déconnexion).
- Ajouter des utilisateurs aux projets et définir leurs rôles.

---

## Technologies

- **Vue.js 3.5.13**
- **Vue Router 4.4.5** pour la gestion des routes.
- **Pinia 2.2.6** pour la gestion de l'état global.
- **Tailwind CSS 3.4.17** pour le design.
- **Axios 1.7.9** pour les appels API.
- **Vite 6.0.1** comme outil de développement.
- **Vee-Validate** et **Yup** pour la validation des formulaires.

---

## Fonctionnalités

- **Tableau Kanban** : Gestion intuitive des colonnes et des tâches.
- **Gestion des utilisateurs** : Authentification, enregistrement et gestion des rôles.
- **Intégration API** : Communication fluide avec l'API backend via Axios.
- **Design réactif** : Interface utilisateur adaptée aux appareils mobiles et aux navigateurs desktop.

---

## Prérequis

- **Node.js**
- **npm** 

---

## Installation

1. Clonez le projet :
   ```bash
   git clone https://github.com/Jeremy-Nourri/titre-pro-projet.git
   cd titre-pro-projet/server
   ```

2. Installez les dépendances :
   ```bash
   npm install
   ```

3. Configurez les variables d'environnement dans un fichier `.env` à la racine du projet :
   ```env
   VITE_API_BASE_URL=http://localhost:8080
   ```

---

## Scripts disponibles

### Développement

Pour démarrer le serveur de développement :
```bash
npm run dev
```

### Compilation

Pour compiler l'application pour la production :
```bash
npm run build
```

### Linter

Pour vérifier et corriger automatiquement le code :
```bash
npm run lint
```

### Tests unitaires

Pour exécuter les tests unitaires avec Vitest :
```bash
npm run test:unit
```

---

## Configuration

### Structure des dossiers

- **src/** : Contient le code source principal.
  - **components/** : Composants réutilisables.
  - **pages/** : Pages principales de l'application.
  - **store/** : Gestion de l'état avec Pinia.
  - **router/** : Fichiers de configuration des routes.

---

## Utilisation

Une fois le projet démarré, accédez à l'application via :
[http://localhost:5173](http://localhost:5173)

---
