# TASK-3.14 — TypeScript Types

## Overview

Define all TypeScript interfaces, enums, and type aliases required by the social-graph feature. These types are the single source of truth for follow-related data shapes across the entire frontend.

## Requirements

- Lives in `frontend/src/types/`.
- No runtime logic — only `interface`, `type`, `enum` declarations.
- All types must match the backend DTOs (`FollowResponse`, `UserSummaryResponse`) exactly.
- Export everything from the file.

## File Location

```
frontend/src/types/follow.ts
```

## Checklist

- [ ] Create `follow.ts` with the following types:

  ```typescript
  // ─── Enums ────────────────────────────────────────────────────────────────

  export enum FollowStatus {
    PENDING = 'PENDING',
    ACCEPTED = 'ACCEPTED',
  }

  // ─── Core domain types ────────────────────────────────────────────────────

  /** Matches backend FollowResponse DTO */
  export interface Follow {
    followerId: string;
    followingId: string;
    status: FollowStatus;
    createdAt: string; // ISO-8601
  }

  /** A pending follow request received by the current user */
  export interface FollowRequest extends Follow {
    /** Populated for convenience — the user who sent the request */
    follower?: UserSummary;
  }

  /** Matches backend UserSummaryResponse DTO */
  export interface UserSummary {
    id: string;
    username: string;
    fullName: string;
    avatarUrl: string | null;
    isVerified: boolean;
    isFollowing: boolean;
  }

  // ─── API payload types ────────────────────────────────────────────────────

  /** Paginated response wrapper for follower / following lists */
  export interface UserSummaryPage {
    content: UserSummary[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    last: boolean;
  }
  ```

- [ ] Verify field names match the backend records (camelCase on frontend, camelCase in Spring `@JsonProperty` defaults)
- [ ] Import and re-use `UserSummary` in `useFollow.ts` hooks (TASK-3.16) without re-declaring it
