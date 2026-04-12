# Coding Standards

All code in this project must follow these conventions consistently, regardless of the layer being worked on.

---

## Backend — Java / Spring Boot

### General Rules

- **Java version:** 21. Use records, sealed classes, pattern matching, and text blocks where appropriate.
- **No framework annotations in the domain layer.** `domain/model/` and `domain/port/` must be pure Java — no `@Entity`, `@Component`, `@Service`, `@Autowired`, or Lombok annotations.
- **Lombok is allowed** only in `adapter/` and `infrastructure/` layers (`@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`).
- **Constructor injection only** — never use field injection (`@Autowired` on fields).
- **`final` fields** for all injected dependencies.

### Naming Conventions

| Artifact | Convention | Example |
|---|---|---|
| Domain entity | Plain noun | `Post`, `User`, `Comment` |
| Use-case interface (in-port) | `<Verb><Noun>UseCase` | `CreatePostUseCase` |
| Service implementing use-case | `<Noun>Service` | `PostService` |
| Out-port (repository) | `<Noun>Repository` | `PostRepository` |
| JPA entity | `<Noun>JpaEntity` | `PostJpaEntity` |
| JPA repository | `<Noun>JpaRepository` | `PostJpaRepository` |
| Persistence adapter | `<Noun>PersistenceAdapter` | `PostPersistenceAdapter` |
| REST Controller | `<Noun>Controller` | `PostController` |
| Request DTO | `<Verb><Noun>Request` | `CreatePostRequest`, `UpdatePostRequest` |
| Response DTO | `<Noun>Response` | `PostResponse` |

### Domain Model

- Domain entities use a **hand-written Builder** pattern (no Lombok) — see `Post.java` as the reference.
- Business behaviour goes **inside the entity** as methods (e.g., `withUpdate()`, `withSoftDelete()`).
- Entities are **immutable via copy** — behaviour methods return new instances.
- No setters on domain entities.

```java
// ✅ Correct
public Post withUpdate(String caption, String location) {
    Post updated = this.copy();
    if (caption != null) updated.caption = caption;
    updated.updatedAt = OffsetDateTime.now();
    return updated;
}

// ❌ Wrong — don't expose setters on domain entities
public void setCaption(String caption) { this.caption = caption; }
```

### Use-Case Interfaces (in-ports)

- One interface per use case, one method per interface.
- Inner `Command` record for input data.

```java
public interface CreatePostUseCase {
    Post createPost(Command command);

    record Command(UUID userId, String caption, String location) {}
}
```

### Request / Response DTOs

- Use Java **records** for DTOs.
- Use Bean Validation annotations (`@NotNull`, `@Size`, `@NotBlank`) on request records.
- Keep response DTOs in `adapter/in/web/dto/`.

```java
// Request
public record CreatePostRequest(
    @Size(max = 2200) String caption,
    @Size(max = 255)  String location
) {}

// Response — factory method pattern
public record PostResponse(UUID id, String caption, ...) {
    public static PostResponse from(Post post) { ... }
}
```

### Controllers

- Annotate with `@RestController` + `@RequestMapping("/api/v1/<resource>")`.
- Always use `@Valid` on request bodies.
- Return `ResponseEntity<T>` — explicit status codes.
- No business logic in controllers — delegate entirely to use-case interfaces.

```java
@PostMapping
public ResponseEntity<PostResponse> create(
        @Valid @RequestBody CreatePostRequest req) {
    Post post = createPostUseCase.createPost(
        new CreatePostUseCase.Command(currentUserId(), req.caption(), req.location()));
    return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(post));
}
```

### Persistence Adapters

- Implement the out-port interface (`implements PostRepository`).
- Contain **all mapping logic** (domain ↔ JPA entity) — keep `toEntity()` and `toDomain()` private methods.
- Never expose `JpaEntity` classes outside the `persistence` package.

### Exception Handling

- Domain exceptions live in `domain/exception/` (e.g., `PostNotFoundException`).
- `GlobalExceptionHandler` (`@RestControllerAdvice`) in `adapter/in/web/` maps them to HTTP responses.
- Never throw `RuntimeException` directly — always use named domain exceptions.

### API Response Format

All endpoints return a consistent envelope (once implemented):

```json
{
  "data": { ... },
  "error": null,
  "timestamp": "2026-04-12T16:00:00Z"
}
```

### Testing

- **Unit tests:** JUnit 5 + Mockito. Test domain services and use-case services in isolation.
- **Integration tests:** `@DataJpaTest` for persistence adapters; `@SpringBootTest` + `MockMvc` for controllers.
- Test class naming: `<ClassUnderTest>Test` (unit), `<ClassUnderTest>IT` (integration).
- Target ≥ 80% coverage for new code.
- Assert on domain behaviour, not implementation details.

---

## Frontend — React / TypeScript

### General Rules

- **TypeScript strict mode** — no `any`, no `as any`. Use proper types from `src/types/`.
- **Functional components only** — no class components.
- **React Query (TanStack)** for all server state — no manual `useEffect` + `useState` for fetching.
- **No direct `fetch` calls** — always use the Axios instance from `src/api/`.
- One file per component. File name matches component name (PascalCase).

### Naming Conventions

| Artifact | Convention | Example |
|---|---|---|
| Component | PascalCase | `PostCard.tsx`, `LikeButton.tsx` |
| Page component | `<Feature>Page` | `PostListPage.tsx`, `LoginPage.tsx` |
| Custom hook | `use<Name>` | `useAuth.ts`, `usePosts.ts` |
| API service file | `<feature>Api.ts` | `postsApi.ts`, `usersApi.ts` |
| TypeScript type/interface | PascalCase | `Post`, `UserProfile`, `ApiResponse<T>` |
| Enum | PascalCase | `PostStatus` |
| Constant | UPPER_SNAKE_CASE | `MAX_CAPTION_LENGTH` |

### Component Structure

```tsx
// 1. Imports (external → internal → types → styles)
import { useState } from 'react';
import { Box, Typography } from '@mui/material';
import { PostCard } from '../components/posts/PostCard';
import type { Post } from '../../types/post';

// 2. Props interface
interface PostListProps {
  userId: string;
}

// 3. Component (named export preferred over default for non-page components)
export function PostList({ userId }: PostListProps) {
  // hooks first
  const { data, isLoading } = usePosts(userId);

  // derived state / handlers
  // JSX return
}
```

### API Layer (`src/api/`)

- One file per domain feature.
- All functions are `async` and return typed data.
- Use the shared Axios instance (with interceptors for JWT injection).

```ts
// postsApi.ts
import { api } from './client';
import type { Post, CreatePostPayload } from '../types/post';

export const postsApi = {
  list: (page = 0) =>
    api.get<Post[]>('/posts', { params: { page } }).then(r => r.data),

  getById: (id: string) =>
    api.get<Post>(`/posts/${id}`).then(r => r.data),

  create: (payload: CreatePostPayload) =>
    api.post<Post>('/posts', payload).then(r => r.data),

  update: (id: string, payload: Partial<CreatePostPayload>) =>
    api.put<Post>(`/posts/${id}`, payload).then(r => r.data),

  remove: (id: string) =>
    api.delete(`/posts/${id}`),
};
```

### React Query Conventions

- Query keys are arrays: `['posts']`, `['posts', id]`, `['users', username, 'posts']`.
- Mutations use `onMutate` for optimistic updates on toggle actions (like, save).
- Always handle `isLoading` and `isError` states in UI.

### MUI Styling

- Use `sx` prop for component-level styles. No inline `style={{}}` objects.
- Use theme tokens (`theme.spacing()`, `theme.palette.primary.main`) — no hardcoded hex colors or pixel values.
- Keep global overrides in `theme.ts`.

### File & Folder Rules

- Components that belong to a specific feature live in `components/<feature>/` (e.g., `components/posts/`).
- Generic shared components live in `components/common/`.
- Each `pages/<feature>/` folder contains only route-level components.

---

## Git Conventions

### Branch Names

```
feat/<short-description>       # new feature
fix/<short-description>        # bug fix
refactor/<short-description>   # code cleanup
chore/<short-description>      # tooling / config
```

### Commit Messages (Conventional Commits)

```
feat(posts): add soft-delete use case
fix(auth): handle expired refresh token 401 response
refactor(domain): extract PostStatus into separate file
chore(ci): add OWASP dependency check step
```

### Pull Requests

- Link to the relevant `plan.md` phase.
- Must pass CI (build + tests) before merge.
- At least one reviewer approval required.
