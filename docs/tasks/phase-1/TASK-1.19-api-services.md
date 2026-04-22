# TASK-1.19 — API Services

## Overview

Create `authApi.ts` and `usersApi.ts` — the frontend API service layer for auth and user operations. These files use the Axios `api` instance (from TASK-0.10) and return typed data matching the types from TASK-1.18.

## Requirements

- Use the shared `api` Axios instance — never `fetch` or a new Axios instance.
- All functions are `async` and return typed data (strip the Axios wrapper).
- Base path: `/api/v1/auth` and `/api/v1/users`.
- No React hooks inside these files — pure async functions only.

## File Locations

```
frontend/src/api/
├── authApi.ts
└── usersApi.ts
```

## Notes

- API functions return the `.data` property of the Axios response (the `ApiResponse<T>.data` field from the backend envelope).
- The Axios response interceptor (TASK-0.10) handles 401 refresh automatically — no retry logic here.
- `usersApi.uploadAvatar` uses `FormData` and sets `Content-Type: multipart/form-data` header.

## Checklist

### `authApi.ts`
- [x] Create `frontend/src/api/authApi.ts`:
  ```ts
  import { api } from './client';
  import type {
    AuthTokens, LoginPayload, RegisterPayload,
    PasswordResetRequestPayload, PasswordResetConfirmPayload
  } from '../types/auth';
  import type { User } from '../types/user';

  export const authApi = {
    register: (payload: RegisterPayload) =>
      api.post<{ data: User }>('/api/v1/auth/register', payload).then(r => r.data.data),

    login: (payload: LoginPayload) =>
      api.post<{ data: AuthTokens }>('/api/v1/auth/login', payload).then(r => r.data.data),

    logout: (refreshToken: string) =>
      api.post('/api/v1/auth/logout', { refreshToken }),

    refresh: (refreshToken: string) =>
      api.post<{ data: AuthTokens }>('/api/v1/auth/refresh', { refreshToken }).then(r => r.data.data),

    requestPasswordReset: (payload: PasswordResetRequestPayload) =>
      api.post('/api/v1/auth/password-reset/request', payload),

    confirmPasswordReset: (payload: PasswordResetConfirmPayload) =>
      api.post('/api/v1/auth/password-reset/confirm', payload),
  };
  ```

### `usersApi.ts`
- [x] Create `frontend/src/api/usersApi.ts`:
  ```ts
  import { api } from './client';
  import type { User, UserProfile, UpdateProfilePayload } from '../types/user';

  export const usersApi = {
    getMe: () =>
      api.get<{ data: User }>('/api/v1/users/me').then(r => r.data.data),

    updateMe: (payload: UpdateProfilePayload) =>
      api.put<{ data: User }>('/api/v1/users/me', payload).then(r => r.data.data),

    getUserByUsername: (username: string) =>
      api.get<{ data: UserProfile }>(`/api/v1/users/${username}`).then(r => r.data.data),

    uploadAvatar: (file: File) => {
      const form = new FormData();
      form.append('file', file);
      return api.put<{ data: User }>('/api/v1/users/me/avatar', form, {
        headers: { 'Content-Type': 'multipart/form-data' },
      }).then(r => r.data.data);
    },
  };
  ```

- [x] Confirm return types are `Promise<User>`, `Promise<AuthTokens>`, `Promise<UserProfile>` — not `Promise<AxiosResponse<...>>`
- [x] Confirm base path matches backend controller mappings (no trailing slash issues)
- [x] Manually test `authApi.register` and `authApi.login` against the running backend after completing TASK-1.13
