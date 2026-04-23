# TASK-2.2 — Domain model: Hashtag

## 📝 Overview

The goal of this task is to implement the **Domain model: Hashtag** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Business Rules
- **Pure Java:** Do not use Spring annotations (like `@Entity`, `@Component`) or Lombok.
- **Immutability:** Use the Builder pattern for object creation. Any business methods that modify state should return a new instance of the model using a `copy()` method.
- **Encapsulation:** Keep constructors private and expose static Builders.
- **Validation:** Ensure required fields are checked for nullability during the `build()` phase.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/domain/model/Hashtag.java
```

## 🧪 Testing Strategy

- **Unit Tests:** Use JUnit 5 and Mockito to test business logic in isolation.
- **Integration Tests:** Use `@DataJpaTest` for repositories and `@SpringBootTest` with `MockMvc` for controllers.

## ✅ Checklist

- [ ] Create `backend/.../domain/model/Hashtag.java`
  - Fields: `id`, `name`, `postCount`
  - Business method: `withIncrementedCount()`
