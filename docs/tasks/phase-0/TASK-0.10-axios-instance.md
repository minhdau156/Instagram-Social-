# TASK-0.10 — Axios Instance with Interceptors

## Overview

Create a shared Axios HTTP client instance (`client.ts`) that all frontend API service files import. The instance automatically attaches the JWT access token to every request and handles `401 Unauthorized` responses by attempting a token refresh — transparently, before propagating the error to the caller.

## Requirements

- Base URL read from the `VITE_API_URL` environment variable.
- Every outgoing request automatically includes `Authorization: Bearer <accessToken>` from localStorage.
- On a `401` response: silently attempt a token refresh, retry the original request once with the new token, and only redirect to `/login` if the refresh itself also fails.
- Exported as a named `api` constant reused by all `*Api.ts` service files.

## Notes

- The refresh logic must use a **queue mechanism** to avoid multiple simultaneous refresh calls when parallel requests all get a 401 at the same time. Store a promise of the in-flight refresh and attach new requests to it.
- The refresh endpoint (`/api/v1/auth/refresh`) must be called **without** the expired access token; it only needs the refresh token (stored separately in localStorage).
- After a successful refresh: update both tokens in localStorage, then retry all queued requests.
- After a failed refresh: clear localStorage, redirect to `/login`, and reject all queued requests.
- Do **not** use `window.location.href` directly — use the React Router `navigate` function. Since `client.ts` is outside the React component tree, you can use a singleton `navigate` ref that is set in `App.tsx` and imported here.

## Checklist

- [ ] Create `frontend/src/api/client.ts`
- [ ] Create Axios instance with `baseURL: import.meta.env.VITE_API_URL`
- [ ] Add request interceptor:
  ```ts
  api.interceptors.request.use(config => {
    const token = localStorage.getItem('accessToken');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  });
  ```
- [ ] Add response interceptor for `401` handling:
  - [ ] If the failing request is itself the refresh endpoint, skip retry (avoid infinite loop)
  - [ ] Otherwise: call refresh token endpoint, store new tokens, retry original request
  - [ ] On refresh failure: clear `localStorage`, call `navigate('/login')`
- [ ] Implement the queue mechanism so concurrent 401s issue only one refresh call
- [ ] Create `frontend/src/lib/navigationRef.ts` — a mutable ref for the navigate function
- [ ] In `App.tsx`: obtain `navigate` from `useNavigate` and set it on `navigationRef` on mount
- [ ] Add `VITE_API_URL=http://localhost:8080` to `frontend/.env.local` (gitignored)
- [ ] Add `VITE_API_URL=` (empty placeholder) to `frontend/.env.example` (committed)
- [ ] Manually test: make an API call with an expired token and verify the token refresh flow works without visible UX disruption
