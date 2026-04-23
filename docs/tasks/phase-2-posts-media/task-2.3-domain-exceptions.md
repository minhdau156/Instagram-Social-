# TASK-2.3 — Domain exceptions

## 📝 Overview

The goal of this task is to implement the **Domain exceptions** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Exception Design
- Create custom unchecked exceptions (extend `RuntimeException`).
- Include meaningful error messages that can be translated or mapped by the GlobalExceptionHandler.
- Optionally include business error codes (e.g. `POST_NOT_FOUND`).

## 📂 File Locations

```text
backend/src/main/java/com/instagram/domain/exception/PostNotFoundException.java
backend/src/main/java/com/instagram/domain/exception/UnauthorizedPostAccessException.java
backend/src/main/java/com/instagram/domain/exception/MediaUploadException.java
```

## 🧪 Testing Strategy

- **Unit Tests:** Use JUnit 5 and Mockito to test business logic in isolation.
- **Integration Tests:** Use `@DataJpaTest` for repositories and `@SpringBootTest` with `MockMvc` for controllers.

## ✅ Checklist

- [ ] Create `backend/.../domain/exception/PostNotFoundException.java`
- [ ] Create `backend/.../domain/exception/UnauthorizedPostAccessException.java`
- [ ] Create `backend/.../domain/exception/MediaUploadException.java`
