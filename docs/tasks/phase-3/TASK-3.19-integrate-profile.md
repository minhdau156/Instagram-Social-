# TASK-3.19 — Integrate FollowButton into ProfilePage

## Overview

Update `PublicProfilePage.tsx` to wire in the `FollowButton` and make the follower/following stat counters interactive (opening the `FollowersDialog` / `FollowingDialog`). This task connects all the social-graph components built in TASK-3.17 into the existing profile page.

## Requirements

- Modify the existing `PublicProfilePage.tsx` (or equivalent) — do **not** create a new page.
- Show `FollowButton` only when viewing another user's profile (not the current user's own profile).
- Follower and following counts must be clickable and open the corresponding dialog.
- Do not break existing profile data display or avatar upload functionality.

## File to Modify

```
frontend/src/pages/profile/PublicProfilePage.tsx
  (check the actual filename — it may be ProfilePage.tsx or UserProfilePage.tsx)
```

## Checklist

### `FollowButton` integration
- [ ] Import `FollowButton` from `components/follow/FollowButton`
- [ ] Import `useAuth` (or equivalent) to get the current logged-in user's username
- [ ] Determine `isOwnProfile`: `currentUser?.username === profileUsername`
- [ ] Render `FollowButton` only when `!isOwnProfile && currentUser !== null`:
  ```tsx
  {!isOwnProfile && (
    <FollowButton
      username={profileUsername}
      initialStatus={profile.isFollowing ? FollowStatus.ACCEPTED : null}
    />
  )}
  ```
  - `profile.isFollowing` comes from the `UserProfile` / `GetUserProfileUseCase` response — confirm this field is populated by the backend

### Follower/Following counter buttons
- [ ] Track dialog open state:
  ```tsx
  const [followersOpen, setFollowersOpen] = useState(false);
  const [followingOpen, setFollowingOpen] = useState(false);
  ```
- [ ] Replace static follower count text with a clickable `Button` (or `ButtonBase`):
  ```tsx
  <Button variant="text" onClick={() => setFollowersOpen(true)}>
    <strong>{profile.stats.followerCount}</strong>&nbsp;followers
  </Button>
  ```
- [ ] Same pattern for the following count
- [ ] Render dialogs at the bottom of the component tree:
  ```tsx
  <FollowersDialog
    username={profileUsername}
    open={followersOpen}
    onClose={() => setFollowersOpen(false)}
  />
  <FollowingDialog
    username={profileUsername}
    open={followingOpen}
    onClose={() => setFollowingOpen(false)}
  />
  ```

### Verify backend `isFollowing` field
- [ ] Confirm the `UserProfile` / profile API response includes an `isFollowing: boolean` field
- [ ] If not, update `GetUserProfileUseCase` and its service implementation to populate it from `FollowRepository.findByFollowerIdAndFollowingId(...)`
