# TASK-2.13 — Tests

## 📝 Overview

The goal of this task is to implement the **Tests** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 📂 File Locations

```text
backend/src/test/java/com/instagram/PostServiceTest.java
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/PostPersistenceAdapterIT.java
backend/src/main/java/com/instagram/infrastructure/web/controller/PostControllerTest.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] `PostServiceTest.java` — create, update, delete, hashtag extraction (unit, Mockito)
- [ ] `PostPersistenceAdapterIT.java` — `@DataJpaTest`, test save / find / soft-delete
- [ ] `PostControllerTest.java` — `@SpringBootTest` + MockMvc, test all CRUD endpoints with auth

---

## Frontend
