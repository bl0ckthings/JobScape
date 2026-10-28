# Démarrer JobScape avec Docker Compose

Depuis la racine `JobScape` :

1. Copier `.env.example` vers `.env` et remplacer les trois valeurs d'exemple par des secrets locaux.
2. Démarrer Docker Desktop.
3. Lancer `docker compose up --build`.

Frontend : http://localhost:4200. Gateway : http://localhost:8080. Consul : http://localhost:8500.

Chaque Dockerfile Java compile son JAR pendant la construction de l'image.

MySQL reste sur le réseau interne Compose ; seul `entity-manager` reçoit son URL et ses identifiants.

Les services Java attendent 3 secondes avant chaque lancement, y compris après un redémarrage sur erreur. Les contrôles de santé de MySQL et Consul sont espacés de 5 secondes.
