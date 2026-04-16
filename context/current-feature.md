# Current Feature: TASK-0.13 — Loading & Skeleton Components

## Status

Not Started

## Goals

- Create `PageLoader` — a full-page centered `CircularProgress` spinner with a fixed backdrop, used during lazy-route Suspense and initial auth checks
- Create `SkeletonCard` — a MUI `Skeleton`-based card that mimics the shape of a `PostCard` (avatar row + image block + caption lines), preventing layout shift
- Create `SkeletonList` — renders `n` `SkeletonCard` instances in a `Stack`, used as the loading fallback for feed lists (`count` prop, default `3`)
- Wire `<PageLoader />` into the `<Suspense fallback={...}>` in `App.tsx` for all lazy routes
- Wire `<SkeletonList />` as the React Query loading fallback in `HomePage`
- Visually verify all three components render correctly in the browser

## Notes

- All three components live in `frontend/src/components/common/`
- Use **MUI only**: `<Skeleton variant="circular|rectangular|text">` and `<CircularProgress>` — no CSS animations or third-party libs
- `PageLoader` uses `position: fixed`, `inset: 0`, `zIndex: 9999`, `bgcolor: 'background.default'`
- `SkeletonCard` dimensions must approximate real `PostCard` (avatar 40px circle, image 300px tall, two caption text lines)
- Components are **purely presentational** — no data props (except `count?: number` on `SkeletonList`)
- Wrap `SkeletonCard` in a `Card` or `Box` with the same padding as a real `PostCard`

## History

- TASK-0.1 — Initialize Project Setup and Configuration
- TASK-0.2 — Makefile Automation Hub
- TASK-0.3 — Docker Local Infrastructure
- TASK-0.4 — Flyway Initial Migration
- TASK-0.5 — GitHub Actions CI Pipeline
- TASK-0.6 — Global Exception Handler
- TASK-0.7 — API Response Wrapper
- TASK-0.8 — BaseEntity Audit Fields
- TASK-0.9 — CORS Configuration
- TASK-0.10 — Axios Instance with Interceptors
- TASK-0.11 — React Query QueryClient Setup
- TASK-0.12 — Error Boundary Component
