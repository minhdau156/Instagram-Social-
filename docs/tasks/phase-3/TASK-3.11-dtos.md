# TASK-3.11 — Request / Response DTOs

## Overview

Create the Data Transfer Objects (DTOs) for the follow feature. These live in the web adapter layer and are responsible for serializing/deserializing HTTP request/response bodies. They must never leak domain objects directly.

## Requirements

- Lives in `adapter/in/web/dto/` (or `adapter/in/web/` if consistent with the existing project convention).
- Use Java records for response DTOs (immutable, concise).
- Provide static `from(DomainObject)` factory methods on response records.
- Use `@Schema` annotations for OpenAPI documentation.

## File Locations

```
backend/src/main/java/com/instagram/adapter/in/web/dto/
├── FollowResponse.java
└── UserSummaryResponse.java
```

## Notes

- `UserSummaryResponse` is **reusable** across the follower list, following list, and anywhere a lightweight user card is needed (e.g., search results, comments). Check if a similar DTO already exists before creating one.
- `FollowResponse` is used for follow, approve, and list-requests endpoints.
- `isFollowing` on `UserSummaryResponse` tells the current viewer whether they already follow this user — this must be computed in the use case layer and passed into the DTO.

## Checklist

### `FollowResponse.java`
- [ ] Create as a Java record:
  ```java
  public record FollowResponse(
      UUID followerId,
      UUID followingId,
      String status,
      Instant createdAt
  ) {
      public static FollowResponse from(Follow follow) {
          return new FollowResponse(
              follow.followerId(),
              follow.followingId(),
              follow.status().name(),
              follow.createdAt()
          );
      }
  }
  ```

### `UserSummaryResponse.java`
- [ ] Create as a Java record:
  ```java
  public record UserSummaryResponse(
      UUID id,
      String username,
      String fullName,
      String avatarUrl,
      boolean isVerified,
      boolean isFollowing
  ) {
      public static UserSummaryResponse from(UserSummary summary) {
          return new UserSummaryResponse(
              summary.id(),
              summary.username(),
              summary.fullName(),
              summary.avatarUrl(),
              summary.isVerified(),
              summary.isFollowing()
          );
      }
  }
  ```

- [ ] Annotate both records with `@Schema(description = "...")` at class level for Swagger docs
- [ ] Annotate individual fields with `@Schema(example = "...")` where useful
