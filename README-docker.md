# Démarrer JobScape avec Docker Compose

Depuis la racine `JobScape` :

1. Copier `.env.example` vers `.env` et remplacer les trois valeurs d'exemple par des secrets locaux.
2. Démarrer Docker Desktop.
3. Lancer `docker compose up --build`.

Frontend : http://localhost:4200. Gateway : http://localhost:8080. Consul : http://localhost:8500.

Chaque Dockerfile Java utilise `maven:3-amazoncorretto-17` pour compiler et tester avec Amazon Corretto 17. Il exécute `mvn clean verify` pendant la construction : compilation, tests et génération du JAR exécutable. Un test en échec arrête la construction. Le profil de test utilise H2 pour les tests de persistance et désactive Consul pendant la construction. L'image finale utilise `amazoncorretto:17` et le JAR produit, sans Maven ni activation du profil de test à l'exécution.

Le frontend construit son application Angular. Ses tests ont été retirés du projet.

MySQL reste sur le réseau interne Compose ; seul `entity-manager` reçoit son URL et ses identifiants.

Les services Java attendent 3 secondes avant chaque lancement, y compris après un redémarrage sur erreur. Les contrôles de santé de MySQL et Consul sont espacés de 5 secondes.

## Générer les JAR avec Maven

Depuis la racine du dépôt :

```sh
./JobScape-backend/entity-manager/mvnw -f JobScape-backend/pom.xml \
  -Dspring.profiles.active=test \
  -Dspring.cloud.consul.enabled=false \
  -Dspring.cloud.consul.discovery.enabled=false \
  clean verify
```

Les JAR sont générés dans le dossier `target` de chaque module. Maven réalise cette étape avec Spring Boot. Jib sert à construire une image de conteneur depuis Maven ; ici, cette construction reste décrite par les Dockerfiles.

## Mesurer la couverture avec JaCoCo

La stratégie et les commandes de test sont détaillées dans [les tests du backend](JobScape-backend/TESTS.md) : 2 à 3 tests unitaires par classe Service, complétés par des parcours d'intégration depuis les contrôleurs.

La commande Maven précédente active JaCoCo dans les six microservices. La phase `verify` génère les rapports HTML, XML et CSV dans `JobScape-backend/<microservice>/target/site/jacoco/`.

Ouvrir `index.html` pour consulter le détail par package et par classe. `jacoco.xml` fournit notamment les compteurs de lignes et de branches couvertes. La couverture est calculée sur les classes de production analysées, sans exclusion de packages configurée dans le projet.

Les Dockerfiles exécutent aussi cette étape. Les rapports restent dans l'étape de construction ; l'image finale contient uniquement le JAR et son environnement d'exécution.

## Publier les images sur Docker Hub

Connecter Docker au compte `oualidteh` :

```sh
docker login --username oualidteh
```

Construire les sept images puis les publier avec la même version :

```sh
./scripts/publish-dockerhub.sh 1.0.0
```

Le script construit d'abord toutes les images. Il exécute `docker compose push` uniquement si toutes les constructions réussissent. Il publie les six services Java et le frontend, sans tenter de publier les images officielles MySQL et Consul.

Exemple de nom publié : `oualidteh/jobscape-web-gateway:1.0.0`.

Le Dockerfile construit l'image. La publication se fait après sa construction, avec les identifiants fournis à `docker login`, sans intégrer ces identifiants dans l'image.

Pour utiliser un autre compte : `DOCKERHUB_USERNAME=autrecompte ./scripts/publish-dockerhub.sh 1.0.0`.

## Récupérer une version publiée

Sur la machine cible, copier ce fichier Compose et préparer le fichier `.env`, puis exécuter :

```sh
DOCKERHUB_USERNAME=oualidteh IMAGE_TAG=1.0.0 docker compose pull
DOCKERHUB_USERNAME=oualidteh IMAGE_TAG=1.0.0 docker compose up -d --no-build
```

Pour récupérer seulement une image : `docker pull oualidteh/jobscape-web-gateway:1.0.0`.

## Compte développeur MySQL

Le script `database/create-dev-user.sql` crée `jobscape_dev` avec les droits DML et DDL sur `jobscape.*`. Remplacer son mot de passe avant de l'exécuter avec un compte administrateur. Le compte développeur permet de modifier le schéma pendant le développement ; le compte applicatif doit rester limité aux opérations nécessaires au fonctionnement de l'application.
