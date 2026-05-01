# TASK-3.17 — Follow Components

## Overview

Create the reusable UI components for the social-graph feature. These components handle all visual states of follow interactions and are used in profile pages, follower/following dialogs, and the follow-request page.

## Requirements

- Lives in `frontend/src/components/follow/`.
- Uses Material UI (MUI) v5 components.
- Each component is focused and reusable — no page-level logic inside.
- Loading states handled with MUI `CircularProgress`.
- Error states handled gracefully (log or show a snackbar, not crash).

## File Locations

```
frontend/src/components/follow/
├── FollowButton.tsx
├── UserListItem.tsx
├── FollowersDialog.tsx
└── FollowingDialog.tsx
```

---

## Checklist

### `FollowButton.tsx`
- [x] Props: `username: string`, `initialStatus: FollowStatus | null`
  - `null` means "not following"
  - `PENDING` means a follow request was sent (private account)
  - `ACCEPTED` means currently following
- [x] Uses `useFollow(username)` hook
- [x] Renders MUI `Button` with the following states:

  | Condition | Button label | Variant | Color |
  |-----------|-------------|---------|-------|
  | Not following | `Follow` | `contained` | `primary` |
  | Pending | `Requested` | `outlined` | `secondary` |
  | Following (not hovered) | `Following` | `outlined` | `primary` |
  | Following (hovered) | `Unfollow` | `outlined` | `error` |
  | Loading | spinner | disabled | — |

- [x] Clicking `Follow` → calls `followMutation.mutate()`
- [x] Clicking `Requested` / `Following` / `Unfollow` → calls `unfollowMutation.mutate()`
- [x] Show `CircularProgress` (size 20) inside the button while mutating

  ```tsx
  export function FollowButton({ username, initialStatus }: FollowButtonProps) {
    const { followMutation, unfollowMutation } = useFollow(username);
    const [hovered, setHovered] = useState(false);
    // ... derive label, variant, color from status + hovered
  }
  ```

---

### `UserListItem.tsx`
- [x] Props: `user: UserSummary`, `currentUsername?: string`
- [x] Renders MUI `ListItem` with:
  - `Avatar` (user avatar, fallback to first letter of username)
  - Primary text: `username` (bold)
  - Secondary text: `fullName`
  - Verified badge (MUI `Tooltip` + small checkmark icon) if `user.isVerified`
  - `FollowButton` on the right (only if `user.username !== currentUsername`)
- [x] Clicking the username/avatar navigates to `/:username` using React Router `useNavigate`

---

### `FollowersDialog.tsx`
- [x] Props: `username: string`, `open: boolean`, `onClose: () => void`
- [x] Uses `useFollowers(username)` hook with infinite scroll
- [x] Renders MUI `Dialog` with:
  - Title: `"Followers"`
  - `DialogContent` with a `List` of `UserListItem` components
  - Infinite scroll: use `IntersectionObserver` (or a sentinel `div` at the bottom) to call `fetchNextPage()` when visible
  - Show loading spinner while `isFetchingNextPage`
  - Show empty state message if no followers

---

### `FollowingDialog.tsx`
- [x] Props: `username: string`, `open: boolean`, `onClose: () => void`
- [x] Same structure as `FollowersDialog` but uses `useFollowing(username)`
- [x] Title: `"Following"`

