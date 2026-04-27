# TASK-3.16 — Custom Hooks

## Overview

Create custom React Query hooks that encapsulate all data-fetching and mutation logic for the social-graph feature. Components consume these hooks — they never call `followApi.ts` directly.

## Requirements

- Lives in `frontend/src/hooks/`.
- Uses `@tanstack/react-query` v5 API (`useQuery`, `useInfiniteQuery`, `useMutation`).
- Cache keys must be stable arrays — define them as constants to avoid typos.
- Optimistic updates on `useFollow` to make the UI feel instant.
- Invalidate relevant cache entries after mutations to keep data fresh.

## File Locations

```
frontend/src/hooks/
├── useFollow.ts
├── useFollowers.ts
├── useFollowing.ts
└── useFollowRequests.ts
```

---

## Checklist

### `useFollow.ts`
- [ ] Export `useFollow(username: string)` hook:
  - Contains `followMutation` (`useMutation` calling `followApi.followUser`)
  - Contains `unfollowMutation` (`useMutation` calling `followApi.unfollowUser`)
  - **Optimistic update**: on `onMutate`, update the cached `UserProfile` or profile query to flip `isFollowing` and adjust follower count
  - **Rollback**: `onError` restores previous snapshot
  - **Settle**: `onSettled` invalidates `['profile', username]` query

  ```typescript
  export function useFollow(username: string) {
    const queryClient = useQueryClient();

    const followMutation = useMutation({
      mutationFn: () => followApi.followUser(username),
      onMutate: async () => {
        await queryClient.cancelQueries({ queryKey: ['profile', username] });
        const snapshot = queryClient.getQueryData(['profile', username]);
        // Apply optimistic update to profile data here
        return { snapshot };
      },
      onError: (_err, _vars, context) => {
        queryClient.setQueryData(['profile', username], context?.snapshot);
      },
      onSettled: () => {
        queryClient.invalidateQueries({ queryKey: ['profile', username] });
      },
    });

    const unfollowMutation = useMutation({
      mutationFn: () => followApi.unfollowUser(username),
      onSettled: () => {
        queryClient.invalidateQueries({ queryKey: ['profile', username] });
      },
    });

    return { followMutation, unfollowMutation };
  }
  ```

---

### `useFollowers.ts`
- [ ] Export `useFollowers(username: string)` hook using `useInfiniteQuery`:
  ```typescript
  export function useFollowers(username: string) {
    return useInfiniteQuery({
      queryKey: ['followers', username],
      queryFn: ({ pageParam = 0 }) => followApi.getFollowers(username, pageParam),
      getNextPageParam: (lastPage, allPages) =>
        lastPage.length < 20 ? undefined : allPages.length,
      initialPageParam: 0,
    });
  }
  ```

---

### `useFollowing.ts`
- [ ] Export `useFollowing(username: string)` hook — same pattern as `useFollowers`, calling `followApi.getFollowing`

---

### `useFollowRequests.ts`
- [ ] Export `useFollowRequests()` hook:
  ```typescript
  export function useFollowRequests() {
    const queryClient = useQueryClient();

    const query = useQuery({
      queryKey: ['followRequests'],
      queryFn: followApi.getFollowRequests,
    });

    const approveMutation = useMutation({
      mutationFn: followApi.approveRequest,
      onSuccess: () => queryClient.invalidateQueries({ queryKey: ['followRequests'] }),
    });

    const declineMutation = useMutation({
      mutationFn: followApi.declineRequest,
      onSuccess: () => queryClient.invalidateQueries({ queryKey: ['followRequests'] }),
    });

    return { ...query, approveMutation, declineMutation };
  }
  ```
