# TASK-3.18 — Follow Requests Page

## Overview

Create `FollowRequestsPage` — a protected page that shows all pending follow requests for the currently authenticated user. Users can approve or decline each request inline.

## Requirements

- Lives in `frontend/src/pages/follow/`.
- Protected route — only accessible to authenticated users (redirect to `/login` otherwise).
- Uses `useFollowRequests()` hook from TASK-3.16.
- Approve/decline actions must give immediate visual feedback (disable buttons while mutating).

## File Location

```
frontend/src/pages/follow/FollowRequestsPage.tsx
```

## Checklist

- [ ] Create `FollowRequestsPage.tsx`:
  ```tsx
  export default function FollowRequestsPage() {
    const { data: requests, isLoading, approveMutation, declineMutation } = useFollowRequests();
    // ...
  }
  ```

- [ ] **Loading state**: show MUI `CircularProgress` centered on the page while `isLoading`

- [ ] **Empty state**: show a centered message `"No pending follow requests"` with a relevant MUI icon (e.g., `PersonAddDisabledOutlined`)

- [ ] **Request list**: render each pending request as a `Card` or `ListItem` containing:
  - `Avatar` with the requester's profile picture (fallback to initials)
  - Requester's `username` (bold, clickable — navigates to `/:username`)
  - Requester's `fullName` (subtitle)
  - **Accept** button (MUI `Button` variant `contained` color `primary`)
    - On click: calls `approveMutation.mutate(requestId)`
    - Disabled + spinner while `approveMutation.isPending` for this specific ID
  - **Decline** button (MUI `Button` variant `outlined` color `error`)
    - On click: calls `declineMutation.mutate(requestId)`
    - Disabled + spinner while `declineMutation.isPending` for this specific ID

- [ ] **Page title**: set `document.title = "Follow Requests | Instagram"` via `useEffect`

- [ ] **Responsive layout**: use MUI `Container maxWidth="sm"` so the list is centred on wide screens

- [ ] **Error state**: show MUI `Alert severity="error"` if the query fails
