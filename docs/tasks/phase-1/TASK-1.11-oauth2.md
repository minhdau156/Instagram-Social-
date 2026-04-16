# TASK-1.11 — OAuth2 (Google / Facebook)

## Overview

Add support for Google and Facebook OAuth2 login. When a user authenticates via OAuth2, the backend upserts a user record, issues a JWT pair, and redirects to the frontend with the tokens in the URL query params.

## Requirements

- Uses Spring Security OAuth2 Client (`spring-boot-starter-oauth2-client`).
- On successful OAuth2 callback: create or update the user record, issue JWT pair, redirect to frontend.
- Placeholder client-id/secret values in `application.yml` (real values go in environment variables / secrets manager).
- The `user_auth_providers` table tracks the linked OAuth2 provider per user.

## File Locations

```
backend/src/main/java/com/instagram/infrastructure/security/OAuth2SuccessHandler.java
backend/src/main/resources/application.yml  (add oauth2 config section)
pom.xml  (add oauth2-client dependency)
```

## Notes

- `OAuth2SuccessHandler` implements `AuthenticationSuccessHandler`.
- Extract the user's email and name from the `OAuth2User` attributes (Google: `email`, `name`; Facebook: `email`, `name`).
- Redirect target: `${frontend.url}/oauth2/callback?accessToken=...&refreshToken=...`
- Store `frontend.url` in `application.yml` (`http://localhost:5173` for dev).
- If a user with the same email already exists, **link the provider** (upsert `user_auth_providers`) rather than creating a duplicate user.
- For now, all OAuth2 users get `ROLE_USER` and a random username derived from their name if no username is set.

## Checklist

### `pom.xml`
- [ ] Add dependency:
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
  </dependency>
  ```

### `application.yml`
- [ ] Add OAuth2 client config block:
  ```yaml
  spring:
    security:
      oauth2:
        client:
          registration:
            google:
              client-id: ${GOOGLE_CLIENT_ID:placeholder}
              client-secret: ${GOOGLE_CLIENT_SECRET:placeholder}
              scope: email, profile
            facebook:
              client-id: ${FACEBOOK_CLIENT_ID:placeholder}
              client-secret: ${FACEBOOK_CLIENT_SECRET:placeholder}
              scope: email, public_profile
  app:
    frontend-url: ${FRONTEND_URL:http://localhost:5173}
  ```

### `OAuth2SuccessHandler.java`
- [ ] Implement `AuthenticationSuccessHandler`
- [ ] Annotate with `@Component`
- [ ] Inject `UserRepository`, `TokenPort`, `PasswordHashPort` (or `UserService` directly via in-port)
- [ ] Implement `onAuthenticationSuccess(request, response, authentication)`:
  - [ ] Cast `authentication.getPrincipal()` to `OAuth2User`
  - [ ] Extract `email` attribute (handle both Google and Facebook attribute keys)
  - [ ] Call `userRepository.findByEmail(email)`:
    - [ ] If present: existing user — skip creation
    - [ ] If absent: build a new `User` (random UUID username `oauth2_<uuid-short>`, status `ACTIVE`, no password hash) → save
  - [ ] Generate access + refresh tokens via `TokenPort`
  - [ ] Build redirect URL: `frontendUrl + "/oauth2/callback?accessToken=" + ... + "&refreshToken=" + ...`
  - [ ] Call `response.sendRedirect(redirectUrl)`

### `SecurityConfig.java` (update from TASK-1.10)
- [ ] Add `.oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler))` to the filter chain

### Frontend route (reference for TASK-1.23)
- [ ] Note that a `/oauth2/callback` route must be added in the frontend (see TASK-1.23) to read the tokens from the URL and store them
