# TASK-1.23 — Register Routes in App.tsx

## Overview

Wire all Phase 1 pages into the React Router configuration. Add public auth routes and protected profile routes, including the OAuth2 callback handler.

## Requirements

- Public routes: `/login`, `/register`, `/forgot-password`, `/reset-password`, `/oauth2/callback`.
- Protected routes (behind `<ProtectedRoute>`): `/profile`, `/profile/:username`.
- Wrap each page with `<ErrorBoundary>` (from TASK-0.12).
- Redirect `/` to `/feed` (Phase 2) for now — stub as a placeholder.

## File Location

```
frontend/src/App.tsx  (update existing file)
```

## Notes

- React Router v6 nested route syntax: use `<Route element={<ProtectedRoute />}>` as a parent, then nest protected child routes inside it.
- The OAuth2 callback page reads `accessToken` and `refreshToken` from the URL query params and stores them in `localStorage`, then navigates to `/`.
- Ensure `<AuthProvider>` wraps the router (already done in TASK-1.20 integration step).

## Checklist

### Public Auth Routes
- [ ] Add `<Route path="/login" element={<ErrorBoundary><LoginPage /></ErrorBoundary>} />`
- [ ] Add `<Route path="/register" element={<ErrorBoundary><RegisterPage /></ErrorBoundary>} />`
- [ ] Add `<Route path="/forgot-password" element={<ErrorBoundary><ForgotPasswordPage /></ErrorBoundary>} />`
- [ ] Add `<Route path="/reset-password" element={<ErrorBoundary><ResetPasswordPage /></ErrorBoundary>} />`

### OAuth2 Callback Route
- [ ] Create inline or separate `OAuth2CallbackPage.tsx`:
  ```tsx
  export function OAuth2CallbackPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
      const accessToken = searchParams.get('accessToken');
      const refreshToken = searchParams.get('refreshToken');
      if (accessToken && refreshToken) {
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        navigate('/', { replace: true });
      } else {
        navigate('/login', { replace: true });
      }
    }, []);

    return <PageLoader />;
  }
  ```
- [ ] Add `<Route path="/oauth2/callback" element={<OAuth2CallbackPage />} />`

### Protected Routes
- [ ] Add a parent `<Route element={<ProtectedRoute />}>` wrapper
- [ ] Inside, add:
  - [ ] `<Route path="/profile" element={<ErrorBoundary><ProfilePage /></ErrorBoundary>} />`
  - [ ] `<Route path="/profile/:username" element={<ErrorBoundary><PublicProfilePage /></ErrorBoundary>} />`

### Redirect & Fallback
- [ ] Add `<Route path="/" element={<Navigate to="/feed" replace />} />` (feed is a stub from Phase 2)
- [ ] Add a `<Route path="*" element={<NotFoundPage />} />` 404 catch-all (create a minimal one-liner `NotFoundPage` if it doesn't exist yet)

### Verification
- [ ] Navigate to `/login` → renders login form ✓
- [ ] Navigate to `/register` → renders register stepper ✓
- [ ] Navigate to `/profile` while logged out → redirects to `/login` ✓
- [ ] Navigate to `/profile` while logged in → renders own profile ✓
- [ ] Navigate to `/profile/someusername` → renders public profile ✓
