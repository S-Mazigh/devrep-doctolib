# Projet Devrep

## Auteurs
- Mazigh Saoudi
- Titouan Polit

## Utilisation
- Pour utiliser une base données pré remplise sans modifier `application.properties`:
  - Lancez le script `createUserBDD.sh` (en faisant attention à lui donner le droit de s'exécuter).
  - Sinon:
    - Créez l'utilisateur `user` dans votre server mysql avec le mot de passe `My%password2` et donnez lui tous les privilèges.
    - Créez la base de données `devrepDoctolib`.
    - Lancez un terminal sur la racine du projet.
    - Lancez la commande `mysql -u user -p devrepDoctolib < ./mysql/dbexport.sql` pour lancer l'import puis entrez le mot de passe `My%password2`.
  - Sachez qu'il s'agit d'une petite base de données où tous les utilisateurs ont comme mot de passe `password`.
  - Vous trouverez la listes des emails dans le fichier **/mysql/lesUtilisateurs.txt**
- Si vous voulez utiliser votre propre base de données:
  - Merci de bien changer le fichier `application.properties` avec:
    - Le bon nom de votre base de données **MySQL**: `spring.datasource.url=jdbc:mysql://localhost/--votreBaseDeDonnées--`
    - Un utilisateur qui a tous les priviléges dans votre base de données: `spring.datasource.username=--votreUsername--` et `spring.datasource.password=--votreMotDePasse--`
- Chargez le projet sur un IDE qui supporte Spring Boot qui vous permettera de lancer le serveur avec un seul clique. L'addresse du home est sur votre localhost à `http://localhost:8080/home`

## Caractéristiques pas encore supportées
- Limiter le nombre de rendez vous pris par utilisateur.
- Interdire à un professionnel de prendre un rendez vous chez lui ou chez un autre professionnel.