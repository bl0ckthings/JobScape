# Authentication TODO List

## Project Folder Structure

Legend: `[exists]` = already in project · `[NEW]` = to create

```
src/
├── main.ts                                                    [exists]
├── index.html                                                 [exists]
├── styles.css                                                 [exists]
│
└── app/
    ├── app.ts                                                 [exists]
    ├── app.html                                               [exists]
    ├── app.css                                                [exists]
    ├── app.routes.ts                                          [exists] ← update with auth routes + guards
    ├── app.config.ts                                          [exists] ← add provideHttpClient() + withInterceptors()
    │
    ├── core/
    │   ├── auth/                                              [NEW]
    │   │   ├── models/
    │   │   │   └── auth.models.ts                            [NEW]  ← LoginRequest, RegisterRequest, AuthResponse, UserProfile
    │   │   ├── services/
    │   │   │   └── auth.service.ts                           [NEW]  ← login, register, logout, isAuthenticated, currentUser signal
    │   │   ├── guards/
    │   │   │   ├── auth.guard.ts                             [NEW]  ← blocks unauthenticated → /login
    │   │   │   └── public.guard.ts                           [NEW]  ← blocks authenticated users from /login and /register
    │   │   └── interceptors/
    │   │       └── jwt.interceptor.ts                        [NEW]  ← attaches Bearer token, handles 401 + refresh
    │   │
    │   └── layout/
    │       ├── app-shell/                                     [exists] ← authenticated layout (keep as-is)
    │       │   ├── app-shell.ts
    │       │   ├── app-shell.html
    │       │   └── app-shell.css
    │       ├── sidebar/                                       [exists] ← update user profile binding + logout
    │       │   ├── sidebar.ts
    │       │   ├── sidebar.html
    │       │   └── sidebar.css
    │       └── public-shell/                                  [NEW]  ← bare layout for login/register (no sidebar)
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
        │   ├── dialogs/
        │   │   ├── application-delete-dialog/
        │   │   ├── application-detail-dialog/
        │   │   ├── application-form-dialog/
        │   │   └── company-form-dialog/
        │   ├── pages/
        │   │   └── applications-board-page/
        │   └── sections/
        │       ├── application-contacts-tab/
        │       ├── application-reminders-tab/
        │       └── application-steps-tab/
        │
        └── auth/                                              [NEW]
            ├── login/
            │   ├── login.ts                                  [NEW]
            │   ├── login.html                                [NEW]
            │   └── login.css                                 [NEW]
            └── register/
                ├── register.ts                               [NEW]
                ├── register.html                             [NEW]
                └── register.css                              [NEW]
```

---

## Routing Plan

```
app.routes.ts
│
├── /                           → redirect to /dashboard
│
├── PublicShell (no guard)
│   ├── /login                  → LoginComponent          (publicGuard — redirect if already authenticated)
│   └── /register               → RegisterComponent       (publicGuard — redirect if already authenticated)
│
└── AppShell (authGuard — redirect to /login if not authenticated)
    ├── /dashboard              → ApplicationsBoardPage
    └── /applications           → ApplicationsBoardPage
```

---

## Environment Files

```
src/
├── environments/                                              [NEW]
│   ├── environment.ts                                        [NEW]  ← { apiUrl: 'http://localhost:8080/api' }
│   └── environment.prod.ts                                   [NEW]  ← { apiUrl: 'https://your-prod-url/api' }
```

---

## 1. Setup HTTP Foundation
- [ ] Add `provideHttpClient()` and `withInterceptors()` to `app.config.ts`
- [ ] Create environment configuration files:
  - `environment.ts`
  - `environment.prod.ts`
- [ ] Configure API base URL in both environment files

---

## 2. Define Authentication Models
Create TypeScript interfaces for:

- [ ] `LoginRequest`
- [ ] `RegisterRequest`
- [ ] `AuthResponse`
  - `accessToken`
  - `refreshToken`
  - `expiresIn`
- [ ] `UserProfile`

---

## 3. Create AuthService
Implement authentication service with the following methods:

- [ ] `login()`
- [ ] `register()`
- [ ] `logout()`
- [ ] `refreshToken()`
- [ ] `isAuthenticated()`
- [ ] `getCurrentUser()`

Additional requirements:

- [ ] Store authentication tokens in `localStorage` or `sessionStorage`
- [ ] Expose a `currentUser` signal

---

## 4. Build JWT HTTP Interceptor
Authentication interceptor responsibilities:

- [ ] Attach Bearer token to all outgoing HTTP requests
- [ ] Handle `401 Unauthorized` responses
- [ ] Attempt token refresh when a `401` occurs
- [ ] Retry the failed request once after successful refresh
- [ ] Redirect to `/login` if refresh fails

---

## 5. Create Route Guards

### Auth Guard (`authGuard`)
- [ ] Create functional route guard
- [ ] Check `AuthService.isAuthenticated()`
- [ ] Redirect unauthenticated users to `/login`

### Public Guard (`publicGuard`)
- [ ] Redirect authenticated users away from:
  - `/login`
  - `/register`

---

## 6. Setup Dual Layout Routing

### Public Layout
- [ ] Create `PublicLayout`
- [ ] No sidebar
- [ ] Centered authentication card
- [ ] Used for:
  - `/login`
  - `/register`

### Authenticated Layout
- [ ] Keep existing `AppShell`
- [ ] Used for protected application routes

### Routing Configuration
- [ ] Configure layouts in `app.routes.ts`
- [ ] Apply appropriate guards to each route group

---

## 7. Build Login Page

### Form Fields
- [ ] Email
- [ ] Password

### Features
- [ ] Use Reactive Forms
- [ ] Add validation messages
- [ ] Submit form via `AuthService.login()`
- [ ] Redirect to `/dashboard` on successful login

---

## 8. Build Register Page

### Form Fields
- [ ] Name
- [ ] Email
- [ ] Password
- [ ] Confirm Password

### Features
- [ ] Use Reactive Forms
- [ ] Add validation messages
- [ ] Implement password confirmation validation
- [ ] Submit form via `AuthService.register()`
- [ ] Redirect to `/login` on successful registration

---

## 9. Integrate User Profile Into Sidebar

### User Information
- [ ] Replace hardcoded values:
  - `"User Doe"`
  - Static email address
- [ ] Bind values from `currentUser` signal

### Logout
- [ ] Connect logout button to `AuthService.logout()`
- [ ] Navigate to `/login` after logout

---

# Completion Criteria

Authentication system is considered complete when:

- [ ] Users can register
- [ ] Users can login
- [ ] JWT tokens are automatically attached to requests
- [ ] Expired tokens refresh automatically
- [ ] Protected routes require authentication
- [ ] Authenticated users cannot access login/register pages
- [ ] User profile information is displayed dynamically
- [ ] Logout fully clears authentication state