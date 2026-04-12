# TASK-0.9 — CORS Configuration

## Overview

Configure Cross-Origin Resource Sharing (CORS) in the Spring Boot backend to allow the React frontend (running on a different port) to make API calls. Without this, all browser requests from `localhost:5173` to `localhost:8080` are blocked by the browser's same-origin policy.

## Requirements

- Allow requests from the React dev server origin (`http://localhost:5173`) in the `local` profile.
- Allow the standard HTTP methods used by the REST API: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`.
- Allow the `Authorization` header so JWT tokens can be sent.
- The CORS config must be applied globally — not per-controller — so new controllers automatically inherit it.
- In `prod`, the allowed origin should come from an environment variable (not hardcoded).

## Notes

- CORS configuration belongs in `infrastructure/config/CorsConfig.java`, not in `SecurityConfig`. Keep concerns separated.
- Spring Security has its own CORS handling — if `SecurityConfig` is present, configure CORS via `HttpSecurity.cors(...)` and provide a `CorsConfigurationSource` bean. A standalone `WebMvcConfigurer` alone may be insufficient when Spring Security is on the classpath.
- `allowedOriginPatterns` is preferred over `allowedOrigins` in Spring Boot 3.x when `allowCredentials = true` is needed.
- `OPTIONS` must be in `allowedMethods` so preflight requests succeed.
- Exposing `Authorization` in `exposedHeaders` is needed if the frontend needs to read it from a response (usually not required).

## Checklist

- [ ] Create `CorsConfig.java` in `backend/.../infrastructure/config/`
  ```java
  @Configuration
  public class CorsConfig {

      @Value("${app.cors.allowed-origins:http://localhost:5173}")
      private List<String> allowedOrigins;

      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(allowedOrigins);
          config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
          config.setAllowedHeaders(List.of("Authorization","Content-Type"));
          config.setAllowCredentials(true);
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/api/**", config);
          return source;
      }
  }
  ```
- [ ] Wire the `CorsConfigurationSource` bean into `SecurityConfig`:
  ```java
  http.cors(cors -> cors.configurationSource(corsConfigurationSource))
  ```
- [ ] Add `app.cors.allowed-origins` to `application-local.yml`:
  ```yaml
  app:
    cors:
      allowed-origins: http://localhost:5173
  ```
- [ ] Add `app.cors.allowed-origins` as an env-var reference in `application-prod.yml`:
  ```yaml
  app:
    cors:
      allowed-origins: ${CORS_ALLOWED_ORIGINS}
  ```
- [ ] Verify with a manual browser request or `curl` from the frontend origin that preflight `OPTIONS` returns `200` with correct `Access-Control-Allow-Origin` header
