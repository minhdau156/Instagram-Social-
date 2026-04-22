# TASK-1.20 — AuthContext & useAuth Hook

## Overview

Create `AuthContext` — the global React context that manages authentication state (current user + tokens) and exposes `login`, `logout`, and `register` functions. Creates a corresponding `useAuth` convenience hook.

## Requirements

- Uses React `Context` + `useReducer` (or `useState`) for state management.
- Persists `accessToken` and `refreshToken` in `localStorage` for session persistence across refreshes.
- On mount, reads tokens from `localStorage` and re-fetches the current user to hydrate the context.
- Exposes `isLoading` flag while the initial auth check is in flight.

## File Locations

```
frontend/src/context/AuthContext.tsx
frontend/src/hooks/useAuth.ts
```

## Notes

- `AuthContext` must be provided at the **root** level (wrap it around `<QueryClientProvider>` and `<RouterProvider>` in `main.tsx` or at the top of `App.tsx`).
- After a successful `login()`, store tokens in `localStorage` AND update context state.
- After `logout()`, clear `localStorage` and reset context state to `null`.
- `isAuthenticated` is a derived boolean: `!!state.user`.
- The initial hydration effect should call `usersApi.getMe()` only if a `accessToken` exists in `localStorage`.

## Checklist

### `AuthContext.tsx`
- [x] Define context shape as an interface:
  ```ts
  interface AuthContextValue {
    user: User | null;
    tokens: AuthTokens | null;
    isLoading: boolean;
    isAuthenticated: boolean;
    login: (payload: LoginPayload) => Promise<void>;
    register: (payload: RegisterPayload) => Promise<void>;
    logout: () => Promise<void>;
  }
  ```

- [x] Create the context with `createContext<AuthContextValue | null>(null)`

- [x] Create `AuthProvider` component:
  - [x] `useState<User | null>(null)` for `user`
  - [x] `useState<AuthTokens | null>(null)` for `tokens`
  - [x] `useState(true)` for `isLoading` (starts as loading until hydration completes)
  - [x] `useEffect` on mount:
    ```ts
    useEffect(() => {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) { setIsLoading(false); return; }
      usersApi.getMe()
        .then(user => setUser(user))
        .catch(() => {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
        })
        .finally(() => setIsLoading(false));
    }, []);
    ```
  - [x] Implement `login(payload)`:
    ```ts
    const login = async (payload: LoginPayload) => {
      const tokens = await authApi.login(payload);
      localStorage.setItem('accessToken', tokens.accessToken);
      localStorage.setItem('refreshToken', tokens.refreshToken);
      setTokens(tokens);
      const user = await usersApi.getMe();
      setUser(user);
    };
    ```
  - [x] Implement `register(payload)`:
    - Call `authApi.register(payload)` → then auto-login (call `login({ identifier: payload.email, password: payload.password })`)
  - [x] Implement `logout()`:
    ```ts
    const logout = async () => {
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) await authApi.logout(refreshToken).catch(() => {});
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      setUser(null);
      setTokens(null);
    };
    ```
  - [x] Return `<AuthContext.Provider value={...}>{children}</AuthContext.Provider>`

- [x] Export `AuthProvider` as a named export
- [x] Export `AuthContext` as a named export

### `useAuth.ts`
- [x] Create `frontend/src/hooks/useAuth.ts`:
  ```ts
  import { useContext } from 'react';
  import { AuthContext } from '../context/AuthContext';

  export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
    return ctx;
  }
  ```

### Integration
- [x] Wrap the app with `<AuthProvider>` in `main.tsx` (or `App.tsx`), outside `<QueryClientProvider>` but inside it if React Query is needed for the initial user fetch

- [x] Verify: refresh the page while logged in — the user stays logged in (hydration from localStorage)
- [x] Verify: open DevTools → clear localStorage → refresh → user is logged out
