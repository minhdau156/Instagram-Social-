# TASK-1.21 — Protected Route Guard

## Overview

Create a `ProtectedRoute` component that gates access to authenticated-only routes. Unauthenticated users are redirected to `/login`; authenticated users pass through to the page.

## Requirements

- Uses `useAuth()` to check authentication state.
- Shows a loading spinner while `isLoading` is `true` (auth state is being hydrated from localStorage).
- Redirects to `/login` if `isAuthenticated` is `false` after loading completes.
- Renders `children` (or `<Outlet />` for nested routes) if authenticated.

## File Location

```
frontend/src/components/common/ProtectedRoute.tsx
```

## Notes

- Use React Router v6's `<Navigate>` for the redirect (not `window.location`).
- Use `<Outlet />` to support nested route layouts — this is the standard pattern for React Router v6 protected routes.
- The loading state must show the `PageLoader` (or a `CircularProgress`) to avoid a flash of the login redirect.
- Pass `state={{ from: location }}` to the `<Navigate>` so that the login page can redirect back after auth.

## Checklist

- [x] Create `frontend/src/components/common/ProtectedRoute.tsx`:
  ```tsx
  import { Navigate, Outlet, useLocation } from 'react-router-dom';
  import { useAuth } from '../../hooks/useAuth';
  import { PageLoader } from './PageLoader';  // from TASK-0.13

  export function ProtectedRoute() {
    const { isAuthenticated, isLoading } = useAuth();
    const location = useLocation();

    if (isLoading) return <PageLoader />;

    if (!isAuthenticated) {
      return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <Outlet />;
  }
  ```

- [x] Export as named export (not default)

- [x] Verify the component handles three states:
  - [x] `isLoading = true` → renders `<PageLoader />`
  - [x] `isLoading = false, isAuthenticated = false` → renders `<Navigate to="/login" />`
  - [x] `isLoading = false, isAuthenticated = true` → renders `<Outlet />`

- [x] Manually test: navigate to `/profile` while logged out → should redirect to `/login`
- [x] Manually test: log in then navigate to `/profile` → should render the profile page
