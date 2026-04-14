# TASK-0.7 — ApiResponse Wrapper

## Overview

Define a single, generic response envelope (`ApiResponse<T>`) that all REST endpoints return. This gives API consumers a consistent contract: they always know where to find data, where to find errors, and what timestamp the response was generated at.

## Requirements

- Must be a Java **record** (Java 21 — per coding standards).
- Generic on `T` so any data type can be wrapped.
- Static factory methods `ok(T data)` and `error(String message)` to avoid constructing the record directly at call sites.
- Used by every controller response and by the `GlobalExceptionHandler`.

## Notes

- Location: `adapter/in/web/dto/ApiResponse.java` — it is a DTO living in the adapter layer (not the domain).
- The `error` field is `null` on success; the `data` field is `null` on error — this is intentional.
- The `timestamp` field uses `java.time.Instant` (serialized to ISO-8601 by Jackson).
- This task is a prerequisite for TASK-0.6 (`GlobalExceptionHandler`). Implement TASK-0.7 first if working on both.
- Controllers that return `ResponseEntity<ApiResponse<T>>` should call `ApiResponse.ok(payload)` to build the body.

## Checklist

- [x] Create `ApiResponse.java` in `backend/.../adapter/in/web/dto/`
  ```java
  public record ApiResponse<T>(
      T data,
      String error,
      Instant timestamp
  ) {
      public static <T> ApiResponse<T> ok(T data) {
          return new ApiResponse<>(data, null, Instant.now());
      }

      public static ApiResponse<Void> error(String message) {
          return new ApiResponse<>(null, message, Instant.now());
      }
  }
  ```
- [x] Verify Jackson serializes `Instant` as an ISO-8601 string (not epoch millis):
  - Set `spring.jackson.serialization.write-dates-as-timestamps=false` in `application.yml`
  - Or configure `ObjectMapper` bean with `JavaTimeModule`
- [x] Update `GlobalExceptionHandler` (TASK-0.6) to return `ApiResponse.error(...)` from all handlers
- [x] Update any existing controllers (e.g., `PostController`) to wrap responses in `ApiResponse.ok(...)`
- [x] Verify the serialized JSON shape matches the expected contract:
  ```json
  {
    "data": { ... },
    "error": null,
    "timestamp": "2026-04-12T09:00:00Z"
  }
  ```
