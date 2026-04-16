# TASK-1.10 — Security Infrastructure (JWT)

## Overview

Implement the JWT-based security infrastructure: a token provider that generates and validates JWTs, a filter that authenticates each request, and the Spring Security configuration that wires it all together.

## Requirements

- `JwtTokenProvider` handles JWT creation and validation using the `jjwt` library.
- `JwtAuthenticationFilter` is a `OncePerRequestFilter` that reads the `Authorization` header on every request.
- `SecurityConfig` defines which endpoints are public vs. protected and registers the filter.

## File Locations

```
backend/src/main/java/com/instagram/infrastructure/security/
├── JwtTokenProvider.java
├── JwtAuthenticationFilter.java
└── SecurityConfig.java
```

## Dependencies to Add in `pom.xml`

```xml
<!-- JJWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

## Configuration Properties (`application.yml`)

```yaml
app:
  jwt:
    secret: <base64-encoded-256-bit-secret>   # set via env var in prod
    access-token-expiry-ms: 900000             # 15 minutes
    refresh-token-expiry-ms: 604800000         # 7 days
```

## Notes

- `JwtTokenProvider` also **implements `TokenPort`** (out-port from TASK-1.6) so the domain service can depend on the port interface.
- Access token claims: `sub = userId`, `role = "ROLE_USER"`.
- Refresh token claims: `sub = userId`, no role claim.
- `JwtAuthenticationFilter` must set `UsernamePasswordAuthenticationToken` in `SecurityContextHolder` on valid tokens.
- Public paths: `POST /api/v1/auth/**`, `GET /api/v1/users/{username}` (public profile), `/swagger-ui/**`, `/v3/api-docs/**`.
- Session management: `STATELESS` — no `HttpSession`.

## Checklist

### `JwtTokenProvider.java`
- [ ] Annotate with `@Component`
- [ ] Inject `@Value("${app.jwt.secret}")` and expiry values
- [ ] Implement `generateAccessToken(UUID userId, String role)`:
  - [ ] Build JWT with `Jwts.builder()`, subject = `userId.toString()`, claim `role`, expiry = now + 15m
  - [ ] Sign with `HS256` using the secret key
- [ ] Implement `generateRefreshToken(UUID userId)`:
  - [ ] Build JWT with subject = `userId.toString()`, expiry = now + 7d
  - [ ] Sign with same key
- [ ] Implement `validateAccessToken(String token)` → `Optional<UUID>`:
  - [ ] Parse and verify signature → extract `sub` → `Optional.of(UUID.fromString(sub))`
  - [ ] On any `JwtException` or `IllegalArgumentException` → return `Optional.empty()`
- [ ] Implement `validateRefreshToken(String token)` → `Optional<UUID>`:
  - [ ] Same as above

### `JwtAuthenticationFilter.java`
- [ ] Extend `OncePerRequestFilter`
- [ ] Annotate with `@Component`
- [ ] Inject `JwtTokenProvider`
- [ ] Implement `doFilterInternal`:
  - [ ] Extract `Authorization` header → check starts with `"Bearer "`
  - [ ] Extract token string → call `tokenProvider.validateAccessToken(token)`
  - [ ] If valid: build `UsernamePasswordAuthenticationToken(userId, null, authorities)` → set in `SecurityContextHolder`
  - [ ] Always call `filterChain.doFilter(request, response)` regardless

### `SecurityConfig.java`
- [ ] Annotate with `@Configuration` and `@EnableWebSecurity`
- [ ] Define `SecurityFilterChain` bean
- [ ] Disable CSRF (stateless API)
- [ ] Set session management to `STATELESS`
- [ ] Configure public matchers:
  - [ ] `POST /api/v1/auth/**`
  - [ ] `GET /api/v1/users/{username}` (public profile)
  - [ ] `/swagger-ui/**`, `/v3/api-docs/**`, `/swagger-ui.html`
  - [ ] `/oauth2/**`, `/login/oauth2/**`
- [ ] Require authentication for all other paths
- [ ] Register `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`
- [ ] Define `PasswordEncoder` bean (BCrypt) — used by `BcryptPasswordHashAdapter` in TASK-1.12
- [ ] Define `AuthenticationManager` bean if needed for OAuth2 config
