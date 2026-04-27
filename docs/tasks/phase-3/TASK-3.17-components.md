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
- [ ] Props: `username: string`, `initialStatus: FollowStatus | null`
  - `null` means "not following"
  - `PENDING` means a follow request was sent (private account)
  - `ACCEPTED` means currently following
- [ ] Uses `useFollow(username)` hook
- [ ] Renders MUI `Button` with the following states:

  | Condition | Button label | Variant | Color |
  |-----------|-------------|---------|-------|
  | Not following | `Follow` | `contained` | `primary` |
  | Pending | `Requested` | `outlined` | `secondary` |
  | Following (not hovered) | `Following` | `outlined` | `primary` |
  | Following (hovered) | `Unfollow` | `outlined` | `error` |
  | Loading | spinner | disabled | — |

- [ ] Clicking `Follow` → calls `followMutation.mutate()`
- [ ] Clicking `Requested` / `Following` / `Unfollow` → calls `unfollowMutation.mutate()`
- [ ] Show `CircularProgress` (size 20) inside the button while mutating

  ```tsx
  export function FollowButton({ username, initialStatus }: FollowButtonProps) {
    const { followMutation, unfollowMutation } = useFollow(username);
    const [hovered, setHovered] = useState(false);
    // ... derive label, variant, color from status + hovered
  }
  ```

---

### `UserListItem.tsx`
- [ ] Props: `user: UserSummary`, `currentUsername?: string`
- [ ] Renders MUI `ListItem` with:
  - `Avatar` (user avatar, fallback to first letter of username)
  - Primary text: `username` (bold)
  - Secondary text: `fullName`
  - Verified badge (MUI `Tooltip` + small checkmark icon) if `user.isVerified`
  - `FollowButton` on the right (only if `user.username !== currentUsername`)
- [ ] Clicking the username/avatar navigates to `/:username` using React Router `useNavigate`

---

### `FollowersDialog.tsx`
- [ ] Props: `username: string`, `open: boolean`, `onClose: () => void`
- [ ] Uses `useFollowers(username)` hook with infinite scroll
- [ ] Renders MUI `Dialog` with:
  - Title: `"Followers"`
  - `DialogContent` with a `List` of `UserListItem` components
  - Infinite scroll: use `IntersectionObserver` (or a sentinel `div` at the bottom) to call `fetchNextPage()` when visible
  - Show loading spinner while `isFetchingNextPage`
  - Show empty state message if no followers

---

### `FollowingDialog.tsx`
- [ ] Props: `username: string`, `open: boolean`, `onClose: () => void`
- [ ] Same structure as `FollowersDialog` but uses `useFollowing(username)`
- [ ] Title: `"Following"`
