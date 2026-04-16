# TASK-0.13 — Loading & Skeleton Components

## Overview

Create a set of reusable loading-state UI components: a full-page spinner for route transitions and a skeleton card family that mimics the shape of content while it loads. Together these prevent layout shifts and give users immediate visual feedback that the app is working.

## Requirements

- `PageLoader` — a full-page centered spinner, used while lazy-loaded routes are resolving or during initial auth check.
- `SkeletonCard` — a single-card skeleton that matches the approximate visual structure of a `PostCard` (avatar row + image block + caption lines).
- `SkeletonList` — renders `n` instances of `SkeletonCard` separated by spacing, used as the loading state for feed lists.

## Notes

- Use MUI's `<Skeleton>` component (`variant="circular"`, `variant="rectangular"`, `variant="text"`) — do not use CSS animations or third-party libraries.
- `SkeletonCard` dimensions should approximate real `PostCard` dimensions to avoid layout shift when data loads.
- `SkeletonList` accepts a `count` prop (default `3`) for flexibility.
- `PageLoader` should use MUI `<CircularProgress>` centered with `position: fixed` and a semi-transparent backdrop so it overlays page content during transitions.
- All three components are **presentational** — no props beyond optional `count` on `SkeletonList`. They accept no data.
- Located in `components/common/` since they are shared across features.

## Checklist

- [x] Create `frontend/src/components/common/PageLoader.tsx`
  ```tsx
  export function PageLoader() {
    return (
      <Box sx={{ position: 'fixed', inset: 0, display: 'flex',
                  alignItems: 'center', justifyContent: 'center',
                  bgcolor: 'background.default', zIndex: 9999 }}>
        <CircularProgress size={48} />
      </Box>
    );
  }
  ```
- [x] Create `frontend/src/components/common/SkeletonCard.tsx`
  - [x] Row: `Skeleton` circle 40px (avatar) + two `Skeleton` text lines (username, timestamp)
  - [x] Block: `Skeleton` rectangular, `width: '100%'`, `height: 300` (post image placeholder)
  - [x] Two short `Skeleton` text lines below (caption placeholder)
  - [x] Wrap in MUI `Card` or `Box` with the same padding as a real `PostCard`
- [x] Create `frontend/src/components/common/SkeletonList.tsx`
  ```tsx
  interface SkeletonListProps {
    count?: number;
  }

  export function SkeletonList({ count = 3 }: SkeletonListProps) {
    return (
      <Stack spacing={2}>
        {Array.from({ length: count }).map((_, i) => (
          <SkeletonCard key={i} />
        ))}
      </Stack>
    );
  }
  ```
- [x] Use `<PageLoader />` in the `<Suspense fallback={...}>` wrapper in `App.tsx` for lazy routes
- [x] Use `<SkeletonList />` as the loading fallback in the feed page (`PostListPage`) while React Query is fetching
- [x] Visually verify all three components in the browser (e.g., add a temporary `isLoading = true` override)
