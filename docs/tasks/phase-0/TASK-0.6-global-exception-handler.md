# TASK-0.6 — Global Exception Handler

## Overview

Implement a centralized exception handler (`@RestControllerAdvice`) that maps all application exceptions to structured HTTP responses. Without this, unhandled exceptions leak stack traces to API clients and return inconsistent error formats.

## Requirements

- All exceptions must be caught and translated to an appropriate HTTP status code.
- Error responses must use the `ApiResponse<Void>` wrapper (implemented in TASK-0.7 — do both tasks together or do TASK-0.7 first).
- Must handle at minimum: domain `NotFoundException` → 404, validation errors → 400, access denied → 403, and all other exceptions → 500.
- Must never expose internal stack traces or class names in the response body.

## Notes

- Class location: `adapter/in/web/GlobalExceptionHandler.java` (the adapter layer is responsible for HTTP concerns).
- Domain exceptions are defined per-feature as they are built (e.g., `PostNotFoundException` in Phase 2). The handler must be easily extensible — new `@ExceptionHandler` methods are added here as new domain exceptions are created.
- Use `log.error(...)` (SLF4J) for `5xx` errors and `log.warn(...)` for `4xx` errors — never `System.out.println`.
- The `@ExceptionHandler(Exception.class)` catch-all should be the last method in the class.
- For `MethodArgumentNotValidException`, extract field-level validation messages and include them in the error string (e.g., `"caption: size must be between 0 and 2200"`).

## Checklist

- [x] Create `GlobalExceptionHandler.java` in `backend/.../adapter/in/web/`
- [x] Annotate the class with `@RestControllerAdvice`
- [x] Inject `Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class)`
- [x] Add handler for `EntityNotFoundException` (or project-level base `NotFoundException`):
  - HTTP `404 Not Found`
  - Body: `ApiResponse.error(e.getMessage())`
- [x] Add handler for `MethodArgumentNotValidException`:
  - HTTP `400 Bad Request`
  - Extract field errors: `e.getBindingResult().getFieldErrors()` → join messages
  - Body: `ApiResponse.error(combinedMessage)`
- [x] Add handler for `ConstraintViolationException`:
  - HTTP `400 Bad Request`
- [x] Add handler for `AccessDeniedException` (Spring Security):
  - HTTP `403 Forbidden`
- [x] Add handler for `HttpMessageNotReadableException` (malformed JSON):
  - HTTP `400 Bad Request`
  - Body: `ApiResponse.error("Malformed request body")`
- [x] Add catch-all handler for `Exception`:
  - HTTP `500 Internal Server Error`
  - Log error with stack trace: `log.error("Unhandled exception", e)`
  - Body: `ApiResponse.error("An unexpected error occurred")`
- [x] Write a unit test `GlobalExceptionHandlerTest` verifying each handler returns the correct HTTP status
