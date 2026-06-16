# Authentication Architecture & File Structure

## Project Structure

```text
src/
├── main.ts                                                    [exists]
├── index.html                                                 [exists]
├── styles.css                                                 [exists]
│
└── app/
    ├── app.ts                                                 [exists]
    ├── app.html                                               [exists]
    ├── app.css                                                [exists]
    ├── app.routes.ts                                          [exists]
    │   └── Update with auth routes and route guards
    │
    ├── app.config.ts                                          [exists]
    │   └── Add provideHttpClient() + withInterceptors()
    │
    ├── core/
    │   ├── auth/                                              [NEW]
    │   │   ├── models/
    │   │   │   └── auth.models.ts
    │   │   │       ├── LoginRequest
    │   │   │       ├── RegisterRequest
    │   │   │       ├── AuthResponse
    │   │   │       └── UserProfile
    │   │   │
    │   │   ├── services/
    │   │   │   └── auth.service.ts
    │   │   │       ├── login()
    │   │   │       ├── register()
    │   │   │       ├── logout()
    │   │   │       ├── refreshToken()
    │   │   │       ├── isAuthenticated()
    │   │   │       ├── getCurrentUser()
    │   │   │       └── currentUser signal
    │   │   │
    │   │   ├── guards/
    │   │   │   ├── auth.guard.ts
    │   │   │   │   └── Redirect unauthenticated users → /login
    │   │   │   │
    │   │   │   └── public.guard.ts
    │   │   │       └── Redirect authenticated users away from auth pages
    │   │   │
    │   │   └── interceptors/
    │   │       └── jwt.interceptor.ts
    │   │           ├── Attach Bearer token
    │   │           ├── Handle 401 responses
    │   │           ├── Refresh token
    │   │           └── Retry failed requests
    │   │
    │   └── layout/
    │       ├── app-shell/                                     [exists]
    │       │   ├── app-shell.ts
    │       │   ├── app-shell.html
    │       │   └── app-shell.css
    │       │
    │       ├── sidebar/                                       [exists]
    │       │   ├── sidebar.ts
    │       │   ├── sidebar.html
    │       │   └── sidebar.css
    │       │
    │       │   Updates:
    │       │   ├── Bind user name/email from AuthService
    │       │   └── Connect logout button
    │       │
    │       └── public-shell/                                  [NEW]
    │           ├── public-shell.ts
    │           ├── public-shell.html
    │           └── public-shell.css
    │
    └── features/
        ├── applications/                                      [exists]
        │   ├── components/
        │   │   ├── application-card/
        │   │   ├── application-card-menu/
        │   │   ├── applications-header/
        │   │   ├── board/
        │   │   ├── sort-filter-bar/
        │   │   └── status-column/
        │   │
        │   ├── dialogs/
        │   │   ├── application-delete-dialog/
        │   │   ├── application-detail-dialog/
        │   │   ├── application-form-dialog/
        │   │   └── company-form-dialog/
        │   │
        │   ├── pages/
        │   │   └── applications-board-page/
        │   │
        │   └── sections/
        │       ├── application-contacts-tab/
        │       ├── application-reminders-tab/
        │       └── application-steps-tab/
        │
        └── auth/                                              [NEW]
            ├── login/
            │   ├── login.ts
            │   ├── login.html
            │   └── login.css
            │
            └── register/
                ├── register.ts
                ├── register.html
                └── register.css
```

---

# Routing Architecture

## Route Hierarchy

```text
/
├── Redirect → /dashboard
│
├── PublicShell
│   ├── /login
│   │   ├── Component: LoginComponent
│   │   └── Guard: publicGuard
│   │
│   └── /register
│       ├── Component: RegisterComponent
│       └── Guard: publicGuard
│
└── AppShell
    ├── Guard: authGuard
    │
    ├── /dashboard
    │   └── ApplicationsBoardPage
    │
    └── /applications
        └── ApplicationsBoardPage
```

---

# Route Behavior

## Public Routes

### `/login`

* Accessible only when unauthenticated
* Redirect authenticated users → `/dashboard`

### `/register`

* Accessible only when unauthenticated
* Redirect authenticated users → `/dashboard`

---

## Protected Routes

### `/dashboard`

* Requires authentication
* Redirect unauthenticated users → `/login`

### `/applications`

* Requires authentication
* Redirect unauthenticated users → `/login`

---

# Environment Configuration

## File Structure

```text
src/
└── environments/
    ├── environment.ts
    └── environment.prod.ts
```

## Development

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## Production

```ts
export const environment = {
  production: true,
  apiUrl: 'https://your-prod-url/api'
};
```

---

# Authentication Flow

```text
User Login
    ↓
AuthService.login()
    ↓
Store accessToken + refreshToken
    ↓
Load currentUser
    ↓
Navigate → /dashboard
    ↓
JWT Interceptor attaches Bearer token
    ↓
Protected API Requests
```

### Token Expiration Flow

```text
API Request
    ↓
401 Unauthorized
    ↓
refreshToken()
    ↓
Success?
    ├── Yes → Retry original request
    └── No  → logout()
                 ↓
             Redirect /login
```

---

# Layout Strategy

## PublicShell

Used for:

* Login
* Register

Characteristics:

* No sidebar
* No navigation
* Centered authentication card
* Minimal layout

---

## AppShell

Used for:

* Dashboard
* Applications
* Future authenticated pages

Characteristics:

* Sidebar navigation
* User profile section
* Logout action
* Full application layout

---

# Sidebar Integration

Replace hardcoded values:

```ts
User Doe
user@example.com
```

With:

```ts
authService.currentUser()?.name
authService.currentUser()?.email
```

Logout button:

```ts
authService.logout();
router.navigate(['/login']);
```
