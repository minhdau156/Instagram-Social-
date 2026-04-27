# TASK-3.2 — Domain Exceptions

## Overview

Create the domain-specific exceptions for the social-graph feature. Each exception maps to a distinct business rule violation and will be translated to an HTTP error response in TASK-3.12.

## Requirements

- All exceptions live in `domain/exception/`.
- Extend `RuntimeException` — never checked exceptions.
- No framework annotations.
- Provide a descriptive message in the constructor so controllers can forward it directly to API error responses.

## File Locations

```
backend/src/main/java/com/instagram/domain/exception/
├── AlreadyFollowingException.java
├── FollowRequestNotFoundException.java
└── CannotFollowYourselfException.java
```

## Notes

- `AlreadyFollowingException` — thrown when a follower attempts to follow a user they already follow (status `ACCEPTED`) or have a pending request for.
- `FollowRequestNotFoundException` — thrown when an approve/decline call references a follow request ID that does not exist or does not belong to the current user.
- `CannotFollowYourselfException` — thrown when `followerId == followingId`.

## Checklist

- [ ] Create `AlreadyFollowingException.java`:
  ```java
  public class AlreadyFollowingException extends RuntimeException {
      public AlreadyFollowingException(String targetUsername) {
          super("Already following or requested to follow user: " + targetUsername);
      }
  }
  ```

- [ ] Create `FollowRequestNotFoundException.java`:
  ```java
  public class FollowRequestNotFoundException extends RuntimeException {
      public FollowRequestNotFoundException(UUID followRequestId) {
          super("Follow request not found: " + followRequestId);
      }
  }
  ```

- [ ] Create `CannotFollowYourselfException.java`:
  ```java
  public class CannotFollowYourselfException extends RuntimeException {
      public CannotFollowYourselfException() {
          super("You cannot follow yourself.");
      }
  }
  ```
