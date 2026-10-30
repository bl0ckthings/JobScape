# Brancher Application Detail dans Angular

> Mise à jour après création du design : le modal, ses onglets et ses formulaires existent maintenant. Ne remplace pas leurs fichiers par les anciennes ébauches des étapes 5 et 6 ci-dessous : cela effacerait le design. Le modal utilise actuellement un modèle de présentation et des données locales ; son adaptation au DTO backend sera traitée après validation du visuel. Aperçu en développement : `/preview/application-detail`.

Objectif : cliquer sur « Voir en détails », ouvrir le modal, charger une seule candidature depuis le gateway, puis afficher Étapes, Contacts et Rappels.

Ce document contient les modifications à réaliser toi-même. Aucun fichier Angular n'a été modifié pour ce guide. Les chemins partent de `JobScape-frontend/src/app/`.

## 1. Aligner la forme du modèle sur le JSON backend

Fichier : `shared/models/applications/application-detail.model.ts`.

Remplace l'interface actuelle qui étend Application. Le backend renvoie un objet imbriqué, avec `application`, `contacts` et `reminders` ; les étapes sont dans `application.steps`.

```ts
import { ApplicationDto } from './applications-by-status-response.model';
import { Contact } from './contact.model';
import { Reminder } from './reminder.model';

export interface ApplicationDetails {
  application: ApplicationDto;
  contacts: Contact[];
  reminders: Reminder[];
}
```

Dans `shared/models/applications/contact.model.ts`, accepte les champs facultatifs renvoyés à null :

```ts
export interface Contact {
  id: number;
  name: string;
  phone: string | null;
  email: string | null;
  role: string | null;
}
```

Dans `shared/models/applications/reminder.model.ts`, aligne le modèle sur ReminderDto :

```ts
export interface Reminder {
  id: number;
  applicationId: number;
  reminderDate: string;
  comment: string | null;
  createdAt: string;
  updatedAt: string;
}
```

Les dates JSON restent des chaînes ISO. DatePipe les formate dans le template ; pas besoin de les convertir en Date pour cette lecture. Le typage TypeScript ne valide pas le JSON à l'exécution : il décrit le contrat attendu.

Vérification : ApplicationDetails contient trois propriétés ; aucune propriété `steps` directement à sa racine.

## 2. Ajouter l'appel HTTP

Fichier : `features/applications/services/application.service.ts`.

Ajoute cet import :

```ts
import { ApplicationDetails } from '../../../shared/models/applications/application-detail.model';
```

Puis cette méthode dans la classe existante :

```ts
getApplicationDetail(id: number): Observable<ApplicationDetails> {
  return this.http.get<ApplicationDetails>(
    `${this.baseUrl}/api/application/${id}/detail`
  );
}
```

HttpClient est déjà injecté dans ce fichier. Aucune nouvelle injection nécessaire ici. L'intercepteur JWT est déjà enregistré dans app.config.ts et ajoute Authorization pour les requêtes vers l'API : ne rajoute ni token ni userId dans cette méthode.

Observable représente le résultat HTTP à venir. L'appel effectif sera déclenché par subscribe dans le dialog. Si tu retournes cet Observable sans le souscrire, rien ne part.

Vérification attendue dans un test HTTP : méthode GET, bon id dans l'URL, aucun userId envoyé par Angular.

## 3. Ajouter l'ouverture du modal dans le service de dialogs

Fichier : `features/applications/services/application-dialog.service.ts`.

Ajoute cet import :

```ts
import { ApplicationDetailDialog } from '../dialogs/application-detail-dialog/application-detail-dialog';
```

Ajoute cette méthode dans la classe existante :

```ts
openDetail(applicationId: number): MatDialogRef<ApplicationDetailDialog, void> {
  return this.dialog.open(ApplicationDetailDialog, {
    width: '600px',
    maxWidth: 'calc(100vw - 24px)',
    maxHeight: 'calc(100dvh - 24px)',
    panelClass: 'jobscape-dialog-panel',
    backdropClass: 'jobscape-dialog-backdrop',
    data: { applicationId },
  });
}
```

MatDialog et MatDialogRef sont déjà importés. MatDialog est déjà injecté : ne l'injecte pas une deuxième fois.

Le champ existant `dialogRef` est typé pour le formulaire création/modification. Ne lui affecte pas ce nouveau dialog ; la nouvelle méthode retourne sa propre référence.

Ici on passe seulement l'id. Le dialog chargera les données à son ouverture. Pas besoin de afterClosed pour une consultation : aucune écriture ne demande de rafraîchir le tableau.

## 4. Brancher le bouton de la carte

Fichier : `features/applications/components/application-card/application-card.ts`.

Ajoute l'import :

```ts
import { ApplicationDialogService } from '../../services/application-dialog.service';
```

Dans la classe, ajoute l'injection et la méthode :

```ts
private readonly applicationDialog = inject(ApplicationDialogService);

openDetail(): void {
  const application = this.application();
  if (!application) return;

  this.closeActions();
  this.applicationDialog.openDetail(application.id);
}
```

`inject` est déjà importé depuis Angular dans ce fichier. La garde évite d'ouvrir un détail sur les cartes de démonstration qui n'ont pas d'objet application.

Fichier : `features/applications/components/application-card/application-card.html`.

Sur le bouton « Voir en détails » uniquement, remplace :

```html
(click)="closeActions()"
```

par :

```html
(click)="openDetail()"
```

Garde les autres attributs et classes du bouton. Ne remplace pas les actions Modifier et Supprimer.

Vérification : le clic appelle openDetail avec l'id réel de la carte et ferme le menu.

## 5. Charger le détail dans le dialog

Fichier : `features/applications/dialogs/application-detail-dialog/application-detail-dialog.ts`.

Remplace le composant vide par :

```ts
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { ApplicationService } from '../../services/application.service';
import { ApplicationDetails } from '../../../../shared/models/applications/application-detail.model';
import { Badge } from '../../../../shared/ui/badge/badge';

@Component({
  selector: 'app-application-detail-dialog',
  imports: [
    DatePipe,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatTabsModule,
    Badge,
  ],
  templateUrl: './application-detail-dialog.html',
  styleUrl: './application-detail-dialog.css',
})
export class ApplicationDetailDialog implements OnInit {
  private readonly applicationService = inject(ApplicationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly data = inject<{ applicationId: number }>(MAT_DIALOG_DATA);

  readonly detail = signal<ApplicationDetails | null>(null);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.loadDetail();
  }

  loadDetail(): void {
    if (this.loading()) return;

    this.loading.set(true);
    this.errorMessage.set(null);
    this.detail.set(null);

    this.applicationService.getApplicationDetail(this.data.applicationId)
      .pipe(
        // Exécuté après succès, erreur ou annulation de la souscription.
        finalize(() => this.loading.set(false)),
        // Fermer le modal annule aussi la requête encore en cours.
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: response => this.detail.set(response),
        error: (error: HttpErrorResponse) => {
          if (error.status === 404) {
            this.errorMessage.set('Cette candidature est introuvable.');
          } else if (error.status === 401 || error.status === 403) {
            this.errorMessage.set('Ta session ou tes droits ne permettent pas cet accès. Reconnecte-toi.');
          } else {
            this.errorMessage.set('Impossible de charger les détails. Réessaie dans un instant.');
          }
        },
      });
  }
}
```

Pourquoi ces injections ?

- ApplicationService donne accès à l'appel HTTP.
- MAT_DIALOG_DATA fournit l'objet `{ applicationId }` passé par openDetail. Ce n'est pas une nouvelle requête HTTP.
- DestroyRef représente la durée de vie de ce dialog : il est détruit lorsqu'on le ferme.

Pourquoi des signals ?

- `detail` contient le résultat. Dans le template, `detail()` lit sa valeur.
- `loading` contrôle le message d'attente.
- `errorMessage` contient un message lisible ou null.
- Appeler `.set(...)` avertit Angular que le template doit être mis à jour.

Le RxJS, ligne par ligne :

1. `getApplicationDetail(...)` fournit l'Observable HTTP.
2. `.pipe(...)` lui ajoute des comportements. Cela ne déclenche pas encore la requête.
3. `finalize(...)` remet loading à false quand la souscription se termine, y compris en cas d'erreur ou de fermeture du modal. Ce n'est pas uniquement un callback de succès.
4. `takeUntilDestroyed(this.destroyRef)` arrête la souscription quand ce dialog est détruit. Avec HttpClient, cela annule une requête encore en cours.
5. `.subscribe(...)` déclenche la requête HTTP.
6. `next` reçoit le JSON réussi et le place dans le signal detail ; Angular affiche les données.
7. `error` reçoit un échec HTTP ou réseau et choisit le message.

Pas besoin de switchMap pour cet appel unique : le chargement commence à l'ouverture du dialog. Le bouton Réessayer rappellera loadDetail après une erreur. L'erreur est déjà gérée dans subscribe ; un catchError qui ferait seulement la même chose serait redondant.

La garde loading empêche deux chargements simultanés. Aucun abonnement imbriqué, aucune souscription dans le template.

## 6. Afficher les trois onglets

Fichier : `features/applications/dialogs/application-detail-dialog/application-detail-dialog.html`.

Remplace le placeholder par cette première version fonctionnelle en lecture seule :

```html
<h2 mat-dialog-title>Détails de la candidature</h2>

<mat-dialog-content [attr.aria-busy]="loading()">
  @if (loading()) {
    <p role="status">Chargement des détails…</p>
  } @else if (errorMessage(); as message) {
    <p role="alert">{{ message }}</p>
    <button type="button" (click)="loadDetail()">Réessayer</button>
  } @else if (detail(); as data) {
    <header class="mb-5">
      <h3 class="text-lg font-bold">{{ data.application.companyName }}</h3>
      <p>{{ data.application.jobTitle }}</p>
      <p>{{ data.application.city }} · {{ data.application.country }}</p>
      <app-badge [status]="data.application.status" />
    </header>

    <mat-tab-group>
      <mat-tab [label]="'Étapes (' + data.application.steps.length + ')'">
        <div class="py-4">
          @for (step of data.application.steps; track step.id) {
            <article class="mb-3 rounded-xl border border-slate-200 p-4">
              <app-badge [status]="step.stepStatus" />
              <p>{{ step.stepDate | date:'dd/MM/yyyy' }}</p>
              <p>{{ step.comment ?? 'Aucun commentaire' }}</p>
            </article>
          } @empty {
            <p>Aucune étape pour cette candidature.</p>
          }
        </div>
      </mat-tab>

      <mat-tab [label]="'Contacts (' + data.contacts.length + ')'">
        <div class="py-4">
          @for (contact of data.contacts; track contact.id) {
            <article class="mb-3 rounded-xl border border-slate-200 p-4">
              <h4 class="font-semibold">{{ contact.name }}</h4>
              <p>{{ contact.role ?? 'Fonction non renseignée' }}</p>
              <p>{{ contact.email ?? 'Email non renseigné' }}</p>
              <p>{{ contact.phone ?? 'Téléphone non renseigné' }}</p>
            </article>
          } @empty {
            <p>Aucun contact pour cette entreprise.</p>
          }
        </div>
      </mat-tab>

      <mat-tab [label]="'Rappels (' + data.reminders.length + ')'">
        <div class="py-4">
          @for (reminder of data.reminders; track reminder.id) {
            <article class="mb-3 rounded-xl border border-slate-200 p-4">
              <p>{{ reminder.reminderDate | date:'dd/MM/yyyy HH:mm' }}</p>
              <p>{{ reminder.comment ?? 'Aucun commentaire' }}</p>
            </article>
          } @empty {
            <p>Aucun rappel pour cette candidature.</p>
          }
        </div>
      </mat-tab>
    </mat-tab-group>
  }
</mat-dialog-content>

<mat-dialog-actions align="end">
  <button type="button" mat-dialog-close>Fermer</button>
</mat-dialog-actions>
```

Les onglets affichent les données déjà chargées. Cliquer sur Contacts ou Rappels ne déclenche pas d'autre HTTP.

Le layout mobile reprend les limites de largeur et hauteur utilisées par ton modal de création ; vérifier le défilement sur une petite hauteur. Cette première version vise le fonctionnement. Les styles existants du panneau sont réutilisés ; la fidélité détaillée à Figma viendra ensuite.

## 7. Adapter les tests, puis vérifier

Fichiers concernés :

- `features/applications/services/application.service.spec.ts` : ajouter un test HttpTestingController sur le GET de détail.
- `features/applications/services/application-dialog.service.spec.ts` : vérifier le composant ouvert et `data.applicationId`.
- `features/applications/components/application-card/application-card.spec.ts` : fournir un mock ApplicationDialogService et vérifier l'id transmis par le clic.
- `features/applications/dialogs/application-detail-dialog/application-detail-dialog.spec.ts` : le test du composant vide doit maintenant fournir MAT_DIALOG_DATA et un mock ApplicationService.

Écris chaque test avant la modification correspondante. Pour le dialog, prévois :

- [ ] Dès l'ouverture : appel HTTP avec le bon id.
- [ ] Réponse réussie : informations de candidature et listes visibles.
- [ ] Réponse en attente : message Chargement visible.
- [ ] Listes vides : messages dédiés.
- [ ] 404 : message introuvable et possibilité de réessayer.
- [ ] Panne réseau / 502 : message d'erreur sans données fictives.
- [ ] Fermeture avant réponse : souscription arrêtée.

Exemple de providers dans le test du dialog, à adapter aux imports du fichier :

```ts
providers: [
  { provide: MAT_DIALOG_DATA, useValue: { applicationId: 12 } },
  {
    provide: ApplicationService,
    useValue: { getApplicationDetail: vi.fn(() => of(detailFixture)) },
  },
]
```

Importer `of` depuis rxjs, `MAT_DIALOG_DATA` depuis @angular/material/dialog et ApplicationService depuis son chemin réel. `detailFixture` est une valeur de test de type ApplicationDetails avec application, contacts et reminders ; ne la laisse pas indéfinie.

Commandes depuis `JobScape-frontend` :

```sh
npm test -- --watch=false
npm run build
```

Puis dans le navigateur :

1. Ouvrir une carte du tableau, menu Actions, Voir en détails.
2. Dans Network : vérifier GET `/api/application/{id}/detail` et Authorization Bearer.
3. Vérifier la réponse : application avec ses steps, contacts et reminders.
4. Ouvrir une autre carte : son id et ses données doivent changer.
5. Vérifier les trois onglets, les collections vides et le mobile.
6. En environnement local avec un second compte, demander le détail d'une candidature du premier compte : 404.

## Faut-il ajouter une route Angular ou modifier le board ?

Non pour ce parcours en modal. Pas de changement dans app.routes.ts, la sidebar ou ApplicationsBoardPage. Le bouton de la carte passe par ApplicationDialogService, puis le dialog appelle ApplicationService. Pas d'ajout dans un NgModule : tes composants sont standalone, leurs dépendances de template vont dans `imports` du décorateur.

Ordre conseillé : modèles ; appel HTTP ; méthode openDetail ; dialog TypeScript + HTML ; clic de carte ; vérification. Pour ton premier petit changement, commence par les modèles et la méthode HTTP, puis vérifie la compilation.
