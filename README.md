# Gestionnaire de Classe

Ce projet est une application web complète permettant de gérer les notes et les devoirs des étudiants. Il est composé d'un backend en Java/SpringBoot et d'un frontend en React.

## Fonctionnalités

*   **Gestion des Classes:**
    *   Création, modification et suppression de classes.
    *   Affichage de la liste des classes.
*   **Gestion des Étudiants:**
    *   Création, modification et suppression d'étudiants.
    *   Attribution d'étudiants à des classes.
    *   Affichage de la liste des étudiants.
*   **Gestion des Matières:**
    *   Création, modification et suppression de matières.
    *   Affichage de la liste des matières.
*   **Gestion des Devoirs:**
    *   Création, modification et suppression de devoirs (CC ou Examen).
    *   Association des devoirs à des classes et des matières.
    *   Définition des parties d'un devoir et de leurs poids.
    *   Affichage de la liste des devoirs.
*   **Gestion des Notations:**
    *   Saisie et modification des notes pour chaque partie d'un devoir, pour chaque étudiant.
    *   Calcul automatique de la note totale pour un devoir.
    *   Calcul de la moyenne par matière et de la moyenne générale pour chaque étudiant.
*   **Bulletin de Notes:**
    *   Affichage d'un bulletin de notes par classe, avec les moyennes par matière et la moyenne générale pour chaque étudiant.

## Technologies

*   **Frontend:**
    *   [Next.js](https://nextjs.org/) (version 15)
    *   [React](https://react.dev/) (version 19)
    *   [shadcn/ui](https://ui.shadcn.com/)
    *   [Tailwind CSS](https://tailwindcss.com/)
    *   [Lucide React](https://lucide.dev/)
    *   [date-fns](https://date-fns.org/)

*   **Backend:**
    *   [Java](https://www.java.com/en/) (version 23)
    *   [SpringBoot](https://spring.io/projects/spring-boot) (version 3.4.1)
    *   [MySQL](https://www.mysql.com/)
    *   [JPA](https://spring.io/projects/spring-data-jpa)

## Architecture

Le projet suit une architecture classique avec une séparation claire entre le frontend et le backend.

*   **Frontend:** L'application frontend est construite avec Next.js et utilise des composants React. Elle communique avec le backend via des appels d'API REST.

*   **Backend:** L'application backend est construite avec SpringBoot et expose des APIs REST pour gérer les données. Elle utilise JPA pour interagir avec la base de données MySQL.

## Installation et Exécution

### Prérequis

*   [Node.js](https://nodejs.org/) (version >= 18)
*   [Java JDK](https://www.oracle.com/java/technologies/downloads/) (version 23)
*   [MySQL](https://www.mysql.com/)
*   [Maven](https://maven.apache.org/)

### Backend

1.  Clonez le dépôt GitHub.
2.  Accédez au répertoire `src/`.
3.  Configurez la connexion à la base de données MySQL dans le fichier `src/main/resources/application.properties`.
4.  Exécutez l'application SpringBoot :  `./mvnw spring-boot:run`.

### Frontend

1.  Clonez le dépôt GitHub (si ce n'est pas déjà fait).
2.  Accédez au répertoire `frontend/`.
3.  Installez les dépendances : `npm install`.
4.  Configurez l'URL du backend dans le fichier `frontend/lib/utils/api.ts`. Assurez vous que le `BACKEND_PATH` pointe bien vers votre instance backend.
5.  Lancez l'application frontend : `npm run dev`.

## Structure du code
.
├── .mvn/ # Configuration Maven Wrapper
├── frontend/ # Code source du frontend (React)
│ ├── app/ # Composants de l'application, routes, etc.
│ ├── components/ # Composants réutilisables
│ ├── hooks/ # Hooks React customisés
│ ├── lib/ # Fonctions utilitaires, types, configurations
│ ├── public/ # Assets statiques (images, etc.)
│ ├── .gitignore # Fichier d'exclusion Git pour le frontend
│ ├── components.json # Configuration shadcn/ui
│ ├── eslint.config.mjs # Configuration ESLint
│ ├── next.config.ts # Configuration React
│ ├── package.json # Dépendances et scripts du frontend
│ ├── postcss.config.mjs # Configuration PostCSS
│ ├── README.md # Ce fichier
│ ├── tailwind.config.ts # Configuration Tailwind CSS
│ └── tsconfig.json # Configuration TypeScript
├── src/ # Code source du backend (Java/SpringBoot)
│ ├── main/
│ │ └── java/
│ │ └── cours/
│ │ └── projetcoursjava/
│ │ ├── controllers/ # Controllers Spring MVC
│ │ ├── entities/ # Entities JPA
│ │ ├── repositories/ # Repositories Spring Data JPA
│ │ ├── services/ # Services métier
│ │ └── types/ # Classes de type Enum ou DTO (Data Transfer Object)
│ └── test/ # Tests unitaires et d'intégration
├── .gitattributes # Attributs Git (gestion des fins de ligne)
├── .gitignore # Fichier d'exclusion Git global
├── mvnw # Maven Wrapper (script shell)
├── mvnw.cmd # Maven Wrapper (script Windows)
└── pom.xml # Fichier de configuration Maven


## API Endpoints

Le backend expose les APIs REST suivantes :

*   **/api/classes**: Gestion des classes.
*   **/api/etudiants**: Gestion des étudiants.
*   **/api/matieres**: Gestion des matières.
*   **/api/devoirs**: Gestion des devoirs.
*   **/api/notation**: Gestion des notations et calcul des moyennes.

Consultez le code source des controllers Spring pour plus de détails sur les endpoints et leurs paramètres.

## Tests

*   Des tests unitaires et d'intégration sont présents dans le backend SpringBoot.

## Auteurs

*   Andrew https://github.com/drewano
*   Léo https://github.com/leodar163
