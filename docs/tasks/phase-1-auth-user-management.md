# Phase 1 — Authentication & User Management

> **Depends on:** Phase 0  
> **Blocks:** Phase 2, 3, 6, 8, 9  
> **BRD refs:** FR-001, FR-002, FR-003, FR-004  
> **DB tables:** `users`, `user_auth_providers`, `password_reset_tokens`, `user_sessions`, `user_stats`  
> **Branch prefix:** `feat/phase-1-`

---

## Backend

### TASK-1.1 — Domain model: User
- [ ] Create `backend/.../domain/model/User.java`
  - Fields (from schema): `id`, `username`, `email`, `phoneNumber`, `passwordHash`, `fullName`, `bio`, `avatarUrl`, `isPrivate`, `isVerified`, `status`, `createdAt`, `updatedAt`
  - Hand-written Builder (no Lombok)
  - Business methods: `withUpdatedProfile(...)`, `withDeactivated()`
  - No framework annotations

### TASK-1.2 — Domain model: UserStats
- [ ] Create `backend/.../domain/model/UserStats.java`
  - Fields: `userId`, `postCount`, `followerCount`, `followingCount`
  - Immutable record or hand-written value object

### TASK-1.3 — Domain exceptions
- [ ] Create `backend/.../domain/exception/UserNotFoundException.java`
- [ ] Create `backend/.../domain/exception/UserAlreadyExistsException.java`
- [ ] Create `backend/.../domain/exception/InvalidCredentialsException.java`
- [ ] Create `backend/.../domain/exception/PasswordResetTokenExpiredException.java`

### TASK-1.4 — Out-port: UserRepository
- [ ] Create `backend/.../domain/port/out/UserRepository.java`
  - Methods: `save(User)`, `findById(UUID)`, `findByUsername(String)`, `findByEmail(String)`, `existsByUsername(String)`, `existsByEmail(String)`

### TASK-1.5 — In-ports (one file each)
- [ ] `domain/port/in/RegisterUserUseCase.java` — inner `Command(username, email, password, fullName)`
- [ ] `domain/port/in/LoginUseCase.java` — inner `Command(identifier, password)`, returns `AuthResult`
- [ ] `domain/port/in/RefreshTokenUseCase.java` — inner `Command(refreshToken)`
- [ ] `domain/port/in/LogoutUseCase.java` — inner `Command(refreshToken)`
- [ ] `domain/port/in/RequestPasswordResetUseCase.java` — inner `Command(email)`
- [ ] `domain/port/in/ConfirmPasswordResetUseCase.java` — inner `Command(token, newPassword)`
- [ ] `domain/port/in/GetUserProfileUseCase.java` — inner `Query(targetUsername, currentUserId)`
- [ ] `domain/port/in/UpdateProfileUseCase.java` — inner `Command(userId, fullName, bio, isPrivate)`

### TASK-1.6 — Domain service: UserService
- [ ] Create `backend/.../domain/service/UserService.java`
  - Implements all in-port interfaces from TASK-1.5
  - Password hashing delegated via `PasswordHashPort` (out-port — avoids BCrypt in domain)
  - Token generation delegated via `TokenPort` (out-port)
  - Inject `UserRepository` (out-port)

### TASK-1.7 — JPA entity: UserJpaEntity
- [ ] Create `backend/.../adapter/out/persistence/UserJpaEntity.java`
  - `@Entity @Table(name = "users")`
  - Extend `BaseJpaEntity`
  - Map all columns exactly as in `schema.sql`
  - `@Column(unique = true)` on `username` and `email`

### TASK-1.8 — JPA repository: UserJpaRepository
- [ ] Create `backend/.../adapter/out/persistence/UserJpaRepository.java`
  - `extends JpaRepository<UserJpaEntity, UUID>`
  - Custom finders: `findByUsername`, `findByEmail`, `existsByUsername`, `existsByEmail`

### TASK-1.9 — Persistence adapter: UserPersistenceAdapter
- [ ] Create `backend/.../adapter/out/persistence/UserPersistenceAdapter.java`
  - `implements UserRepository`
  - Private `toEntity(User)` and `toDomain(UserJpaEntity)` mapping methods
  - No JPA entities exposed outside this class

### TASK-1.10 — Security infrastructure
- [ ] Create `backend/.../infrastructure/security/JwtTokenProvider.java`
  - `generateAccessToken(UUID userId, String role)` → signed JWT (15 min expiry)
  - `generateRefreshToken(UUID userId)` → signed JWT (7 days expiry)
  - `validateToken(String token)` → `Optional<Claims>`
- [ ] Create `backend/.../infrastructure/security/JwtAuthenticationFilter.java`
  - `extends OncePerRequestFilter`
  - Extracts Bearer token → validates → sets `SecurityContextHolder`
- [ ] Create `backend/.../infrastructure/config/SecurityConfig.java`
  - Configure filter chain: public paths (`/api/v1/auth/**`), protected paths
  - Register `JwtAuthenticationFilter`
  - Define `ROLE_USER`, `ROLE_ADMIN`

### TASK-1.11 — OAuth2 (Google/Facebook)
- [ ] Create `backend/.../infrastructure/security/OAuth2SuccessHandler.java`
  - On successful OAuth2 callback: upsert user in DB, issue JWT pair, redirect to frontend
- [ ] Add OAuth2 dependency to `pom.xml`: `spring-boot-starter-oauth2-client`
- [ ] Add OAuth2 config properties to `application.yml` (placeholder client-id/secret)

### TASK-1.12 — Password hash & email adapters (out-ports)
- [ ] Create `backend/.../domain/port/out/PasswordHashPort.java` interface (`hash`, `verify`)
- [ ] Create `backend/.../adapter/out/security/BcryptPasswordHashAdapter.java` (implements it)
- [ ] Create `backend/.../domain/port/out/EmailPort.java` interface (`sendPasswordResetEmail`)
- [ ] Create `backend/.../adapter/out/email/SmtpEmailAdapter.java` (stub for now, logs email links)

### TASK-1.13 — REST controllers
- [ ] Create `backend/.../adapter/in/web/AuthController.java`
  - `POST /api/v1/auth/register`
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/refresh`
  - `POST /api/v1/auth/logout`
  - `POST /api/v1/auth/password-reset/request`
  - `POST /api/v1/auth/password-reset/confirm`
- [ ] Create `backend/.../adapter/in/web/UserController.java`
  - `GET /api/v1/users/me`
  - `PUT /api/v1/users/me`
  - `GET /api/v1/users/{username}`

### TASK-1.14 — Request/Response DTOs
- [ ] `RegisterRequest.java` — `username`, `email`, `password` (`@NotBlank`, `@Email`, `@Size(min=8)`)
- [ ] `LoginRequest.java` — `identifier`, `password`
- [ ] `AuthResponse.java` — `accessToken`, `refreshToken`, `expiresIn`
- [ ] `UpdateProfileRequest.java` — `fullName`, `bio`, `isPrivate`
- [ ] `UserResponse.java` — `id`, `username`, `fullName`, `bio`, `avatarUrl`, `isPrivate`, `isVerified`, factory `from(User)`

### TASK-1.15 — Avatar upload
- [ ] Create `backend/.../domain/port/out/MediaStoragePort.java`
  - Method: `uploadFile(String key, byte[] data, String contentType) → String url`
- [ ] Create `backend/.../adapter/out/storage/MinioStorageAdapter.java`
  - Implements `MediaStoragePort` using MinIO Java SDK
- [ ] Add `PUT /api/v1/users/me/avatar` endpoint to `UserController` (multipart upload)

### TASK-1.16 — Register `GlobalExceptionHandler` mappings for auth exceptions
- [ ] Map `UserNotFoundException` → `404`
- [ ] Map `UserAlreadyExistsException` → `409`
- [ ] Map `InvalidCredentialsException` → `401`
- [ ] Map `PasswordResetTokenExpiredException` → `400`

### TASK-1.17 — Unit & integration tests
- [ ] `UserServiceTest.java` — register, login, password reset (Mockito mocks for ports)
- [ ] `UserPersistenceAdapterIT.java` — `@DataJpaTest`, test CRUD and finder queries
- [ ] `AuthControllerTest.java` — `@SpringBootTest` + MockMvc, test register/login/refresh endpoints

---

## Frontend

### TASK-1.18 — TypeScript types
- [ ] Create `frontend/src/types/auth.ts` — `AuthTokens`, `LoginPayload`, `RegisterPayload`
- [ ] Create `frontend/src/types/user.ts` — `User`, `UserProfile`, `UpdateProfilePayload`

### TASK-1.19 — API services
- [ ] Create `frontend/src/api/authApi.ts` — `register`, `login`, `logout`, `refresh`, `requestPasswordReset`, `confirmPasswordReset`
- [ ] Create `frontend/src/api/usersApi.ts` — `getMe`, `updateMe`, `getUserByUsername`, `uploadAvatar`

### TASK-1.20 — AuthContext & useAuth hook
- [ ] Create `frontend/src/context/AuthContext.tsx`
  - Stores `user`, `tokens` in context
  - Provides `login()`, `logout()`, `register()` functions
  - Persists access/refresh tokens in localStorage
- [ ] Create `frontend/src/hooks/useAuth.ts` — convenience hook for `AuthContext`

### TASK-1.21 — Protected route guard
- [ ] Create `frontend/src/components/common/ProtectedRoute.tsx`
  - Redirects to `/login` if `useAuth()` returns no token
  - Shows `PageLoader` while auth is resolving

### TASK-1.22 — Pages
- [ ] Create `frontend/src/pages/auth/LoginPage.tsx`
  - Email/password form with MUI `TextField` + `Button`
  - Google / Facebook OAuth2 login buttons (link to backend `/oauth2/authorize/google`)
  - Link to `/register` and `/forgot-password`
- [ ] Create `frontend/src/pages/auth/RegisterPage.tsx`
  - Multi-step form: step 1 (email/phone) → step 2 (username/password) → step 3 (full name)
- [ ] Create `frontend/src/pages/auth/ForgotPasswordPage.tsx`
  - Email input → POST to `password-reset/request` → success message
- [ ] Create `frontend/src/pages/auth/ResetPasswordPage.tsx`
  - Reads `token` from URL query param → new password form
- [ ] Create `frontend/src/pages/profile/ProfilePage.tsx`
  - Own profile: avatar, stats, edit button, post grid
- [ ] Create `frontend/src/pages/profile/PublicProfilePage.tsx`
  - Other user's profile: avatar, stats, follow button, post grid

### TASK-1.23 — Register routes in App.tsx
- [ ] Add routes: `/login`, `/register`, `/forgot-password`, `/reset-password`
- [ ] Add protected routes: `/profile`, `/profile/:username`
