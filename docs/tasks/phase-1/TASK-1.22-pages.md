# TASK-1.22 — Auth & Profile Pages

## Overview

Create all page-level components for auth flows (login, register, forgot-password, reset-password) and user profiles (own profile and public profile). These are route-level components only — no shared logic, no data definitions.

## Requirements

- Functional components only.
- MUI components for all form controls (`TextField`, `Button`, `Alert`).
- React Hook Form (`useForm`) for form state and validation.
- React Query mutations for API calls (use `useMutation` from `@tanstack/react-query`).
- `useAuth()` hook for `login()`, `register()` actions.

## File Locations

```
frontend/src/pages/
├── auth/
│   ├── LoginPage.tsx
│   ├── RegisterPage.tsx
│   ├── ForgotPasswordPage.tsx
│   └── ResetPasswordPage.tsx
└── profile/
    ├── ProfilePage.tsx
    └── PublicProfilePage.tsx
```

## Notes

- Install React Hook Form if not already present: `npm install react-hook-form`.
- All pages must display an `<Alert severity="error">` when the API call fails.
- `RegisterPage` is a multi-step form (MUI `Stepper`).
- `ProfilePage` shows the current user's own profile with an edit button.
- `PublicProfilePage` shows any user by `:username` URL param.
- Avatar display uses MUI `Avatar` component.
- Stats (posts / followers / following) use a simple horizontal flex row.

## Checklist

### `LoginPage.tsx`
- [ ] Form fields: `identifier` (email or username), `password`
- [ ] Submit calls `auth.login(payload)` from `useAuth()` → on success navigate to `/`
- [ ] Show `CircularProgress` on the submit button while pending
- [ ] Google OAuth2 button: `<Button href="/oauth2/authorize/google">Continue with Google</Button>`
- [ ] Facebook OAuth2 button: `<Button href="/oauth2/authorize/facebook">Continue with Facebook</Button>`
- [ ] Footer links: `"Don't have an account? Sign up"` → `/register` | `"Forgot password?"` → `/forgot-password`
- [ ] Page title: `<title>Login — Instagram</title>` (use `document.title` or a `Helmet` if installed)

### `RegisterPage.tsx`
- [ ] MUI `Stepper` with 3 steps:
  - [ ] Step 1 — `email` (with `@Email` validation) and `phoneNumber` (optional)
  - [ ] Step 2 — `username` (3–30 chars, alphanumeric + underscores) and `password` (min 8 chars)
  - [ ] Step 3 — `fullName` (required)
- [ ] Back/Next buttons; submit on step 3 completion
- [ ] On submit: call `auth.register(payload)` from `useAuth()` → on success navigate to `/`
- [ ] Link: `"Already have an account? Log in"` → `/login`

### `ForgotPasswordPage.tsx`
- [ ] Single `email` field
- [ ] On submit: call `authApi.requestPasswordReset({ email })` directly (no auth context needed)
- [ ] On success: show a green `Alert`: `"If that email exists, a reset link has been sent."`
- [ ] Link: `"Back to login"` → `/login`

### `ResetPasswordPage.tsx`
- [ ] Read `token` from URL query params: `useSearchParams()`
- [ ] Form fields: `newPassword` + `confirmPassword` (client-side equality check)
- [ ] On submit: call `authApi.confirmPasswordReset({ token, newPassword })`
- [ ] On success: navigate to `/login` with a success message in router state
- [ ] If `token` param is missing: show error `Alert` immediately

### `ProfilePage.tsx`
- [ ] Uses `useAuth()` to get `user`
- [ ] Displays: `Avatar`, username, fullName, bio, `isPrivate` badge if true
- [ ] Stats row: `postCount | followerCount | followingCount` (stubbed as `0` until Phase 3)
- [ ] Edit Profile button → opens a `Dialog` (or navigates to `/settings/profile`) with `UpdateProfileRequest` fields
- [ ] On save: call `usersApi.updateMe(payload)` and refresh user in context
- [ ] Post grid: stub `<Box>No posts yet</Box>` (Phase 2 will fill this)

### `PublicProfilePage.tsx`
- [ ] Reads `:username` from route params: `useParams()`
- [ ] Uses `useQuery(['users', username], () => usersApi.getUserByUsername(username))` from React Query
- [ ] Shows `<PageLoader />` while loading; `<ErrorMessage />` on error
- [ ] Displays: `Avatar`, username, fullName, bio, stats row
- [ ] Follow button (stubbed — disabled in Phase 1, enabled in Phase 3): `<Button disabled>Follow</Button>`
- [ ] If current user IS the profile owner, show "Edit Profile" instead of "Follow"
