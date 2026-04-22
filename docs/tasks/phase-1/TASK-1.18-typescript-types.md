# TASK-1.18 — TypeScript Types

## Overview

Define all TypeScript interfaces and types for the auth and user-management feature. All other frontend files in Phase 1 import from these type files — create them first.

## Requirements

- Strict TypeScript — no `any`.
- Use `interface` for objects with methods/extensibility; `type` for unions/aliases.
- All files in `frontend/src/types/`.

## File Locations

```
frontend/src/types/
├── auth.ts
└── user.ts
```

## Notes

- `AuthTokens` matches the `AuthResponse` DTO from the backend.
- `User` matches `UserResponse` from the backend.
- `UserProfile` wraps `User` with stats and follow state — matches `UserProfileResponse`.
- These types are the **source of truth** on the frontend — API functions must return these exact shapes.

## Checklist

### `auth.ts`
- [x] Create `frontend/src/types/auth.ts`:
  ```ts
  export interface AuthTokens {
    accessToken: string;
    refreshToken: string;
    expiresIn: number;  // seconds
  }

  export interface LoginPayload {
    identifier: string;  // email or username
    password: string;
  }

  export interface RegisterPayload {
    username: string;
    email: string;
    password: string;
    fullName: string;
  }

  export interface PasswordResetRequestPayload {
    email: string;
  }

  export interface PasswordResetConfirmPayload {
    token: string;
    newPassword: string;
  }
  ```

### `user.ts`
- [x] Create `frontend/src/types/user.ts`:
  ```ts
  export interface User {
    id: string;
    username: string;
    email: string;
    fullName: string;
    bio: string | null;
    avatarUrl: string | null;
    isPrivate: boolean;
    isVerified: boolean;
  }

  export interface UserProfile {
    user: User;
    postCount: number;
    followerCount: number;
    followingCount: number;
    isFollowing: boolean;
  }

  export interface UpdateProfilePayload {
    fullName?: string;
    bio?: string | null;
    isPrivate?: boolean;
  }
  ```

- [x] Confirm all types are exported as **named exports** (no default exports in type files)
- [x] Confirm `bio` and `avatarUrl` on `User` are `string | null` (matching backend nullable columns)
