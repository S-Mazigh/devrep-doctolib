# Projet Devrep

## Auteurs
    - Mazigh Saoudi
    - Titouan Polit

## Utilisation
- Merci de bien changer le fichier `application.properties` avec:
  - Le bon nom de votre base de données **MySQL**: `spring.datasource.url=jdbc:mysql://localhost/--votreBaseDeDonnées--`
  - Un utilisateur qui a tous les priviléges dans votre base de données: `spring.datasource.username=--votreUsername--` et `spring.datasource.password=--votreMotDePasse--`
- Chargez le projet sur un IDE qui supporte Spring Boot qui vous permettera de lancer le serveur avec un seul clique. L'addresse du home est sur votre localhost à `http://localhost:8080/home`

## Caractéristiques pas encore supportées
- Suppression d'une entité: Utilisateur ou Rendez-vous.
- Limiter le nombre de rendez vous pris par utilisateur.
- Interdire à un professionnel de prendre un rendez vous chez lui ou chez un autre professionnel.