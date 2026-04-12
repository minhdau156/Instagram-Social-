# TASK-0.11 — React Query QueryClient Setup

## Overview

Install and configure TanStack React Query (v5) as the global server-state manager. React Query replaces manual `useEffect` + `useState` data fetching patterns with a declarative, cache-aware approach. This setup task prepares the `QueryClient` and wraps the entire app so every component can use `useQuery` and `useMutation` hooks.

## Requirements

- `@tanstack/react-query` must be installed as a production dependency.
- A singleton `QueryClient` with sensible defaults (stale time, retry policy).
- The entire app is wrapped in `<QueryClientProvider>` in `main.tsx`.
- `ReactQueryDevtools` available in local/dev mode (not bundled in production).

## Notes

- Use **React Query v5** (`@tanstack/react-query@^5`) — the API differs from v4 (e.g., `useQuery` now takes a single object argument).
- Recommended default settings for this app:
  - `staleTime: 1000 * 60` (1 minute) — most data doesn't change that fast.
  - `retry: 1` — retry failed requests once before showing an error.
  - `refetchOnWindowFocus: false` — avoids surprising re-fetches while the user switches tabs.
- `ReactQueryDevtools` is imported from `@tanstack/react-query-devtools` (separate package). Show it only when `import.meta.env.DEV` is true to keep production bundle clean.
- The `QueryClient` instance is created **outside** of any React component so it is a true singleton and survives re-renders.

## Checklist

- [ ] Install dependencies:
  ```bash
  npm i @tanstack/react-query
  npm i -D @tanstack/react-query-devtools
  ```
- [ ] Create `frontend/src/lib/queryClient.ts`:
  ```ts
  import { QueryClient } from '@tanstack/react-query';

  export const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 1000 * 60,
        retry: 1,
        refetchOnWindowFocus: false,
      },
    },
  });
  ```
- [ ] Update `frontend/src/main.tsx`:
  - [ ] Import `{ QueryClientProvider }` from `@tanstack/react-query`
  - [ ] Import `{ queryClient }` from `./lib/queryClient`
  - [ ] Wrap `<App />` with `<QueryClientProvider client={queryClient}>`
  - [ ] Conditionally render `<ReactQueryDevtools initialIsOpen={false} />` inside the provider when `import.meta.env.DEV`
- [ ] Verify the devtools panel appears in the browser in local dev mode
- [ ] Verify no devtools import appears in the production build (`npm run build` output)
