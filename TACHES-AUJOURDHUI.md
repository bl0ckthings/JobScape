# JobScape — jeudi 29 octobre 2026

Objectif : cliquer sur « Voir en détails » et consulter les vraies données de la candidature dans les trois onglets Étapes, Contacts et Rappels.

État vérifié dans le code : création et liste présentes ; détail incomplet ; ContactClient et ContactController de company-service vides ; modal de détail vide. Cette checklist est un plan local, pas une mise à jour GitHub. Les tests n'ont pas été relancés pour cette préparation.

Les chemins backend ci-dessous partent de JobScape-backend ; ceux du frontend de JobScape-frontend/src/app. Pour chaque comportement : écrire le test, constater son échec attendu, coder le minimum, vérifier le test puis refactoriser si nécessaire. Le premier lancement des tests application-service peut échouer à la compilation à cause du retour manquant : ce n'est pas encore un test fonctionnel rouge.

## 1. Fixer le contrat de détail — première tâche

Fichiers : application-service/.../dto/application/ApplicationDetailDto.java ; web-gateway/.../service/applicationDto/ApplicationDetailDto.java ; frontend shared/models/applications/application-detail.model.ts.

- [ ] Garder la forme backend existante : un objet `application`, une liste `contacts`, une liste `reminders`.
- [ ] Garder les étapes dans `application.steps` pour cette livraison : ApplicationDto les contient déjà. Éviter une deuxième liste d'étapes au même niveau.
- [ ] Adapter le modèle Angular au JSON imbriqué. Actuellement ApplicationDetails étend Application : cette forme ne correspond pas au DTO backend.
- [ ] Vérifier les champs utilisés par le modal : entreprise, logo, poste, statut, lieu, date, étapes, contacts et rappels.
- [ ] Définir les collections vides comme `[]`. Aucun résultat doit permettre un écran vide normal.

Terminé quand : les contrats Angular, gateway et application-service décrivent le même JSON. Séparer le DTO de liste et retirer ses étapes restera une tâche suivante.

## 2. Remettre application-service en état de compiler et protéger le détail

Fichier principal : application-service/.../service/ApplicationService.java ; tests : application-service/.../service/ApplicationServiceTest.java.

- [ ] Ajouter les tests de base : propriétaire autorisé ; autre utilisateur refusé ; candidature inexistante.
- [ ] Compléter `getApplicationDetail(applicationId, userId)` : charger la candidature, vérifier le propriétaire, construire et retourner ApplicationDetailDto.
- [ ] Conserver le contrôle de propriété avant les appels contacts et rappels. Tester qu'aucun appel à ces services n'est effectué pour un autre utilisateur.
- [ ] Ne jamais accepter userId depuis le formulaire du navigateur : il vient du JWT extrait par le gateway.
- [ ] Retourner 404 pour une candidature absente ou appartenant à quelqu'un d'autre.

Terminé quand : application-service compile et les tests d'accès passent. Les enrichissements contacts/rappels sont complétés dans les deux tâches suivantes ; des listes temporairement vides ne signifient pas que le détail est fini.

## 3. Relier les contacts à travers company-service

Fichiers : entity-manager/.../repository/ContactRepository.java, service/ContactService.java, controller/ContactController.java ; company-service/.../client/ContactClient.java, controller/ContactController.java et service dédié ; application-service : client contacts à ajouter.

- [ ] Utiliser la signature existante `findByCompany_IdAndUser_IdOrderByCreatedAtAscIdAsc(companyId, userId)`.
- [ ] Ajouter le service et la route entity-manager qui utilisent cette recherche. La route actuelle par entreprise seule ne filtre pas par utilisateur.
- [ ] Tester : une entreprise commune à deux utilisateurs ne doit pas partager leurs contacts privés.
- [ ] Compléter ContactClient dans company-service avec exactement la même méthode HTTP, route et paramètres que cette nouvelle route interne.
- [ ] Faire passer le contrôleur company-service par un service ; convertir les résultats en ContactSummaryDto selon le contrat choisi.
- [ ] Ajouter le client Feign dans application-service vers company-service.
- [ ] Après le contrôle de propriété, rechercher les contacts avec companyId de la candidature et userId authentifié.
- [ ] Affecter le résultat à `detail.contacts` ; retourner `[]` si aucun contact.
- [ ] Tester l'ordre : ancien vers récent, puis id pour départager deux dates identiques.

Terminé quand : le détail contient uniquement les contacts de l'utilisateur pour cette entreprise. Le modèle actuel est par entreprise + utilisateur : il ne permet pas à lui seul de limiter aux contacts spécifiquement rattachés à cette candidature.

## 4. Charger les rappels

Fichiers : reminder-service : contrôleur/service de lecture ; application-service/.../client/ReminderClient.java et service/ApplicationService.java.

- [ ] Vérifier la route de lecture existante par applicationId dans reminder-service et son DTO de réponse.
- [ ] Ajouter cette lecture dans ReminderClient. Actuellement ce client expose uniquement la création.
- [ ] Aligner route, méthode HTTP, nom du paramètre et type `List<ReminderDto>`.
- [ ] Appeler cette méthode après vérification du propriétaire de la candidature.
- [ ] Affecter le résultat à `detail.reminders` ; aucun rappel donne `[]`.
- [ ] Vérifier l'ordre voulu : date de rappel de la plus ancienne à la plus récente.
- [ ] Tester : plusieurs rappels, aucun rappel et service indisponible. Une panne ne doit pas apparaître comme une liste vide réussie.

Terminé quand : ApplicationDetailDto contient candidature, étapes, contacts et rappels réels.

## 5. Brancher les endpoints et les erreurs du gateway

Fichiers : application-service/.../controller/ApplicationController.java ; web-gateway/.../client/ApplicationClient.java et controller/ApplicationController.java ; couche d'exceptions du gateway.

- [ ] Remplacer le `return null` du contrôleur application-service par l'appel au service de détail.
- [ ] Corriger le client gateway : il déclare actuellement POST alors que le contrôleur application-service attend GET sur `/{id}/detail`.
- [ ] Garder le contrat interne GET avec applicationId dans le chemin et userId en paramètre.
- [ ] Remplacer le `return null` du gateway : lire authenticatedUserId et transmettre id + userId au client Feign.
- [ ] Utiliser la route publique déjà déclarée : `GET /api/application/detail/{id}`.
- [ ] Vérifier comment les erreurs Feign sont traduites ; empêcher qu'un 404 du service devienne un 500 générique.
- [ ] Renvoyer au frontend un corps d'erreur cohérent avec les conventions existantes, sans stack trace.
- [ ] Tester : propriétaire = 200 ; autre utilisateur = 404 ; inexistant = 404 ; sans authentification = réponse du mécanisme de sécurité ; service en panne = erreur explicite.

Terminé quand : un appel authentifié au gateway renvoie le JSON complet ; un autre utilisateur n'obtient aucune donnée.

## 6. Brancher le modal Angular en lecture seule

Fichiers : features/applications/services/application.service.ts ; service d'ouverture des dialogs ; carte ou page qui déclenche « Voir en détails » ; dialogs/application-detail-dialog/application-detail-dialog.ts, .html et .css ; modèle de détail.

- [ ] Ajouter `getApplicationDetail(id)` dans le service HTTP Angular avec le modèle aligné à l'étape 1.
- [ ] Faire transmettre l'id réel de la carte au dialog lors du clic « Voir en détails ».
- [ ] Charger le détail à l'ouverture du dialog ; éviter de charger tous les détails lors de l'affichage du tableau.
- [ ] Prévoir trois états visibles : chargement, résultat, erreur avec bouton Réessayer.
- [ ] Afficher l'en-tête : nom et logo de l'entreprise, poste, statut et informations disponibles.
- [ ] Afficher `application.steps` dans Étapes, `contacts` dans Contacts, `reminders` dans Rappels.
- [ ] Prévoir les états « Aucune étape », « Aucun contact » et « Aucun rappel ».
- [ ] Prévoir une image de remplacement si logo absent ou inaccessible.
- [ ] Afficher un message adapté pour 404 et pour une panne du serveur.
- [ ] Vérifier fermeture, défilement, tabs et affichage mobile sans débordement horizontal.
- [ ] Tester le chargement HTTP, la réponse vide, l'erreur et le rendu des trois listes.

Terminé quand : les données du modal changent selon la carte sélectionnée et proviennent de l'API. Ajout/modification/suppression dans les onglets viendront ensuite.

## 7. Valider le parcours complet

- [ ] Dans chaque module backend modifié : `./mvnw test`, puis `./mvnw -DskipTests package`.
- [ ] Dans JobScape-frontend : `npm test -- --watch=false`, puis `npm run build`.
- [ ] Vérifier les scripts disponibles : aucun script lint dédié dans package.json actuellement. Ne pas annoncer un lint réussi.
- [ ] Reconstruire les services Docker modifiés avec `docker compose up -d --build` depuis la racine.
- [ ] Avec un premier compte : ouvrir une candidature, vérifier ses étapes, contacts et rappels.
- [ ] Vérifier un cas sans contact ni rappel.
- [ ] Avec un deuxième compte : demander le détail du premier compte et vérifier le refus.
- [ ] Arrêter temporairement un service nécessaire en environnement local, vérifier une erreur lisible puis le redémarrer et tester Réessayer.
- [ ] Vérifier que création et liste fonctionnent encore.

## Après ce parcours

- [ ] Séparer le DTO léger du tableau du DTO complet de détail ; ne plus transporter toutes les étapes dans la liste.
- [ ] Brancher l'ajout d'étape et actualiser le statut de la candidature.
- [ ] Brancher les formulaires contacts et rappels.
- [ ] Brancher modification et suppression avec contrôle du propriétaire.
- [ ] Rendre le tri réellement fonctionnel, puis construire le dashboard et les notifications.
- [ ] Traiter la création partielle si une étape ou un rappel échoue après enregistrement de la candidature : sujet distinct à planifier.

Priorité ce soir : finir les points 1 à 5 avant de peaufiner le modal. Si le temps le permet, réaliser 6 et 7. L'ensemble constitue une tranche fonctionnelle complète ; ce n'est pas une promesse de durée pour une soirée.
