# TASK-1.12 — Password Hash & Email Adapters (Out-Ports)

## Overview

Create the out-port interfaces for password hashing and email sending, then implement the concrete adapters. These adapters keep framework specifics (BCrypt, SMTP) out of the domain layer.

## Requirements

- `PasswordHashPort` interface in `domain/port/out/` — no Spring or BCrypt imports.
- `BcryptPasswordHashAdapter` in `adapter/out/security/` — implements `PasswordHashPort` using `PasswordEncoder`.
- `EmailPort` interface in `domain/port/out/` — no JavaMail imports.
- `SmtpEmailAdapter` in `adapter/out/email/` — **stub** for Phase 1: just logs the reset link to the console.

## File Locations

```
backend/src/main/java/com/instagram/domain/port/out/
├── PasswordHashPort.java
└── EmailPort.java

backend/src/main/java/com/instagram/adapter/out/
├── security/BcryptPasswordHashAdapter.java
└── email/SmtpEmailAdapter.java
```

## Notes

- `PasswordHashPort.verify(rawPassword, storedHash)` should return `boolean` — no exception on mismatch (the service throws `InvalidCredentialsException` instead).
- `BcryptPasswordHashAdapter` uses the `PasswordEncoder` bean defined in `SecurityConfig` (TASK-1.10) — inject it via constructor.
- `SmtpEmailAdapter`: log the reset URL at `INFO` level — this is a stub until a real mail service is configured.
- The email stub should log: `"[EMAIL STUB] Password reset link: {link}"`.

## Checklist

### `PasswordHashPort.java`
- [x] Create interface in `domain/port/out/`:
  ```java
  public interface PasswordHashPort {
      String hash(String rawPassword);
      boolean verify(String rawPassword, String storedHash);
  }
  ```
- [x] Confirm no BCrypt or Spring imports

### `BcryptPasswordHashAdapter.java`
- [x] Create class in `adapter/out/security/`:
  ```java
  @Component
  @RequiredArgsConstructor
  public class BcryptPasswordHashAdapter implements PasswordHashPort {
      private final PasswordEncoder passwordEncoder;

      @Override
      public String hash(String rawPassword) {
          return passwordEncoder.encode(rawPassword);
      }

      @Override
      public boolean verify(String rawPassword, String storedHash) {
          return passwordEncoder.matches(rawPassword, storedHash);
      }
  }
  ```
- [x] `PasswordEncoder` bean is injected from `SecurityConfig` (BCryptPasswordEncoder)

### `EmailPort.java`
- [x] Create interface in `domain/port/out/`:
  ```java
  public interface EmailPort {
      void sendPasswordResetEmail(String toEmail, String resetToken);
  }
  ```
- [x] Confirm no JavaMail or SMTP imports

### `SmtpEmailAdapter.java`
- [x] Create stub class in `adapter/out/email/`:
  ```java
  @Component
  @Slf4j
  public class SmtpEmailAdapter implements EmailPort {

      @Value("${app.frontend-url:http://localhost:5173}")
      private String frontendUrl;

      @Override
      public void sendPasswordResetEmail(String toEmail, String resetToken) {
          String link = frontendUrl + "/reset-password?token=" + resetToken;
          log.info("[EMAIL STUB] Password reset link for {}: {}", toEmail, link);
      }
  }
  ```
