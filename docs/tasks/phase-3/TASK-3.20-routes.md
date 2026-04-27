# TASK-3.20 — Register Routes in App.tsx

## Overview

Add the `/follow-requests` protected route to `App.tsx` (or the router configuration file) so that `FollowRequestsPage` is reachable. This is the final wiring step for the Phase 3 social-graph feature.

## Requirements

- Add the route inside the existing React Router `<Routes>` tree.
- Wrap with the existing `ProtectedRoute` guard component (from Phase 1) to redirect unauthenticated users to `/login`.
- Do **not** restructure existing routes — append only.

## File to Modify

```
frontend/src/App.tsx
  (or wherever the router <Routes> are defined — check App.tsx or router.tsx)
```

## Checklist

- [ ] Import `FollowRequestsPage`:
  ```tsx
  import FollowRequestsPage from './pages/follow/FollowRequestsPage';
  ```

- [ ] Add protected route inside the existing `<Routes>`:
  ```tsx
  <Route
    path="/follow-requests"
    element={
      <ProtectedRoute>
        <FollowRequestsPage />
      </ProtectedRoute>
    }
  />
  ```

- [ ] Verify the route is accessible:
  - Navigate to `http://localhost:5173/follow-requests` while logged in → page loads
  - Navigate while logged out → redirected to `/login`

- [ ] (Optional) Add a notification badge / link to `FollowRequestsPage` in `AppShell.tsx` (sidebar or top navigation) for discoverability:
  ```tsx
  // Example: show badge with pending request count
  <IconButton component={Link} to="/follow-requests">
    <Badge badgeContent={pendingCount} color="error">
      <PersonAddOutlined />
    </Badge>
  </IconButton>
  ```
  - Use `useFollowRequests()` at the shell level and pass `data?.length ?? 0` as `pendingCount`
