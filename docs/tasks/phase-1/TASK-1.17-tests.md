# TASK-1.17 — Unit & Integration Tests

## Overview

Write comprehensive tests for the auth and user-management feature. Tests cover the domain service in isolation (unit tests) and the persistence adapter + REST controllers with real Spring context (integration tests).

## Requirements

- Unit tests use JUnit 5 + Mockito — no Spring context loaded.
- Integration tests use `@DataJpaTest` (persistence) and `@SpringBootTest` + MockMvc (controllers).
- Target **≥ 80% coverage** for all Phase 1 classes.
- All test class names follow the convention: `<ClassUnderTest>Test` (unit) and `<ClassUnderTest>IT` (integration).

## File Locations

```
backend/src/test/java/com/instagram/
├── domain/service/UserServiceTest.java          (unit)
├── adapter/out/persistence/UserPersistenceAdapterIT.java  (integration)
└── adapter/in/web/AuthControllerIT.java         (integration)
```

## Checklist

### `UserServiceTest.java` (Unit)

Setup:
- [ ] Annotate with `@ExtendWith(MockitoExtension.class)`
- [ ] Declare `@Mock` fields: `UserRepository`, `PasswordHashPort`, `TokenPort`, `EmailPort`
- [ ] Declare `@InjectMocks UserService userService`

Register tests:
- [ ] `register_success_returnsUser()` — happy path; verify `userRepository.save()` is called once
- [ ] `register_throwsException_whenUsernameExists()` — stub `existsByUsername = true`, expect `UserAlreadyExistsException`
- [ ] `register_throwsException_whenEmailExists()` — stub `existsByEmail = true`, expect `UserAlreadyExistsException`

Login tests:
- [ ] `login_success_returnsAuthResult()` — stub `findByEmail` with active user, `verify = true`
- [ ] `login_throwsException_whenUserNotFound()` — both finders return `Optional.empty()`
- [ ] `login_throwsException_whenPasswordInvalid()` — user found but `verify = false`
- [ ] `login_throwsException_whenUserDeactivated()` — user status is `DEACTIVATED`

Password reset tests:
- [ ] `requestReset_silentlyIgnores_whenEmailNotFound()` — verify `emailPort` is never called
- [ ] `requestReset_sendsEmail_whenUserFound()` — verify `emailPort.sendPasswordResetEmail()` called once

Update profile tests:
- [ ] `updateProfile_success_returnsSavedUser()`
- [ ] `updateProfile_throwsException_whenUserNotFound()`

### `UserPersistenceAdapterIT.java` (Integration — `@DataJpaTest`)

Setup:
- [ ] Annotate with `@DataJpaTest`
- [ ] Inject `UserJpaRepository` (Spring autowires it)
- [ ] Instantiate `UserPersistenceAdapter` manually (or `@Autowired`)

Tests:
- [ ] `save_andFindById_success()` — save a user, find by ID, assert fields match
- [ ] `findByUsername_returnsUser_whenExists()`
- [ ] `findByEmail_returnsUser_whenExists()`
- [ ] `findByUsername_returnsEmpty_whenNotFound()`
- [ ] `existsByUsername_returnsTrue_whenExists()`
- [ ] `existsByEmail_returnsFalse_whenNotExists()`
- [ ] `save_updatesExistingUser()` — save, mutate via domain method, save again, verify update

### `AuthControllerIT.java` (Integration — `@SpringBootTest + MockMvc`)

Setup:
- [ ] Annotate with `@SpringBootTest(webEnvironment = RANDOM_PORT)` and `@AutoConfigureMockMvc`
- [ ] Use `@MockBean` to mock `RegisterUserUseCase`, `LoginUseCase`, etc. (test only HTTP layer)
- [ ] Inject `MockMvc`

Register tests:
- [ ] `POST /api/v1/auth/register_returns201_onSuccess()`
- [ ] `POST /api/v1/auth/register_returns400_whenUsernameBlank()`
- [ ] `POST /api/v1/auth/register_returns400_whenPasswordTooShort()` (`< 8` chars)
- [ ] `POST /api/v1/auth/register_returns409_whenUserAlreadyExists()` — stub useCase to throw `UserAlreadyExistsException`

Login tests:
- [ ] `POST /api/v1/auth/login_returns200_withTokens_onSuccess()`
- [ ] `POST /api/v1/auth/login_returns401_onInvalidCredentials()`

Refresh tests:
- [ ] `POST /api/v1/auth/refresh_returns200_withNewTokens()`
- [ ] `POST /api/v1/auth/refresh_returns401_whenTokenInvalid()`

Password reset tests:
- [ ] `POST /api/v1/auth/password-reset/request_returns200_always()` — even with non-existent email

User controller tests (add to a separate `UserControllerIT.java`):
- [ ] `GET /api/v1/users/{username}_returns200_forPublicProfile()`
- [ ] `GET /api/v1/users/me_returns401_whenUnauthenticated()`
- [ ] `PUT /api/v1/users/me_returns200_withUpdatedProfile()` — provide valid JWT in header
