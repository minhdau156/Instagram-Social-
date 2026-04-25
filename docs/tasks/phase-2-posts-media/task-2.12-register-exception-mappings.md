# TASK-2.12 — Register exception mappings

## 📝 Overview

The goal of this task is to implement the **Register exception mappings** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// Inside GlobalExceptionHandler.java (add to existing handler class)
@ExceptionHandler(PostNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handlePostNotFound(PostNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiErrorResponse.of("POST_NOT_FOUND", ex.getMessage()));
}

@ExceptionHandler(UnauthorizedPostAccessException.class)
public ResponseEntity<ApiErrorResponse> handleUnauthorizedPostAccess(
        UnauthorizedPostAccessException ex) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ApiErrorResponse.of("FORBIDDEN", ex.getMessage()));
}

@ExceptionHandler(MediaUploadException.class)
public ResponseEntity<ApiErrorResponse> handleMediaUpload(MediaUploadException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiErrorResponse.of("MEDIA_UPLOAD_FAILED", ex.getMessage()));
}
```

## ✅ Checklist

- [ ] Map `PostNotFoundException` → `404`
- [ ] Map `UnauthorizedPostAccessException` → `403`
- [ ] Map `MediaUploadException` → `500`
