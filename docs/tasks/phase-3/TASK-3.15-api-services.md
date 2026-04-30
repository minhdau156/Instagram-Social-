# TASK-3.15 — API Services

## Overview

Create `followApi.ts` — the Axios-based API service layer for all follow-related HTTP calls. This file is the **only** place that knows about the backend URL paths for the follow feature.

## Requirements

- Lives in `frontend/src/api/`.
- Uses the shared `axiosInstance` (already configured with base URL and auth interceptor).
- All functions are `async` and return typed promises.
- Never handle errors here — let React Query (`useQuery`, `useMutation`) handle them.
- No state management — pure HTTP call wrappers.

## File Location

```
frontend/src/api/followApi.ts
```

## Checklist

- [x] Create `followApi.ts` with the following functions:

  ```typescript
  import axiosInstance from './axiosInstance';
  import type { Follow, UserSummary, FollowRequest } from '../types/follow';

  const BASE = '/api/v1';

  /** Follow a user by username. Returns the created Follow object. */
  export async function followUser(username: string): Promise<Follow> {
    const { data } = await axiosInstance.post<{ data: Follow }>(
      `${BASE}/users/${username}/follow`
    );
    return data.data;
  }

  /** Unfollow a user by username. */
  export async function unfollowUser(username: string): Promise<void> {
    await axiosInstance.delete(`${BASE}/users/${username}/follow`);
  }

  /** Get paginated followers of a user. */
  export async function getFollowers(
    username: string,
    page = 0,
    size = 20
  ): Promise<UserSummary[]> {
    const { data } = await axiosInstance.get<{ data: UserSummary[] }>(
      `${BASE}/users/${username}/followers`,
      { params: { page, size } }
    );
    return data.data;
  }

  /** Get paginated following list of a user. */
  export async function getFollowing(
    username: string,
    page = 0,
    size = 20
  ): Promise<UserSummary[]> {
    const { data } = await axiosInstance.get<{ data: UserSummary[] }>(
      `${BASE}/users/${username}/following`,
      { params: { page, size } }
    );
    return data.data;
  }

  /** Get all pending follow requests for the current user. */
  export async function getFollowRequests(): Promise<FollowRequest[]> {
    const { data } = await axiosInstance.get<{ data: FollowRequest[] }>(
      `${BASE}/follow-requests`
    );
    return data.data;
  }

  /** Approve a follow request by its ID. */
  export async function approveRequest(requestId: string): Promise<Follow> {
    const { data } = await axiosInstance.post<{ data: Follow }>(
      `${BASE}/follow-requests/${requestId}/approve`
    );
    return data.data;
  }

  /** Decline a follow request by its ID. */
  export async function declineRequest(requestId: string): Promise<void> {
    await axiosInstance.delete(`${BASE}/follow-requests/${requestId}`);
  }
  ```

- [x] Verify the response envelope shape matches the backend `ApiResponse<T>` wrapper (i.e., `response.data.data`)
- [x] Confirm the `axiosInstance` import path matches the existing project convention (check `postsApi.ts` or `mediaApi.ts` for reference)
