# TASK-1.15 — Avatar Upload

## Overview

Enable users to upload a profile avatar image. The file is stored in MinIO (local dev) / S3 (production) and the resulting URL is saved on the user record.

## Requirements

- New out-port `MediaStoragePort` for file upload abstraction.
- `MinioStorageAdapter` implements the port using MinIO Java SDK.
- `PUT /api/v1/users/me/avatar` accepts `multipart/form-data`.
- Returns the updated `UserResponse` with the new `avatarUrl`.

## File Locations

```
backend/src/main/java/com/instagram/domain/port/out/MediaStoragePort.java
backend/src/main/java/com/instagram/adapter/out/storage/MinioStorageAdapter.java
backend/src/main/java/com/instagram/adapter/in/web/ (update UserController.java)
```

## Dependencies to Add in `pom.xml`

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.11</version>
</dependency>
```

## Configuration Properties (`application.yml`)

```yaml
app:
  minio:
    endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
    access-key: ${MINIO_ACCESS_KEY:minioadmin}
    secret-key: ${MINIO_SECRET_KEY:minioadmin}
    bucket: ${MINIO_BUCKET:instagram-media}
```

## Notes

- The storage key (object name) for avatars: `avatars/{userId}/{uuid}.{extension}`.
- The returned URL is the public download URL from MinIO.
- `MediaStoragePort` will be reused by Phase 2 (post media upload).
- MaxFile size: configure `spring.servlet.multipart.max-file-size=10MB` in `application.yml`.
- Allowed MIME types: `image/jpeg`, `image/png`, `image/webp` — validate in the controller.

## Checklist

### `MediaStoragePort.java`
- [ ] Create interface in `domain/port/out/`:
  ```java
  public interface MediaStoragePort {
      /**
       * Uploads a file and returns the public URL.
       * @param key     object storage key (path/filename)
       * @param data    file bytes
       * @param contentType MIME type (e.g. "image/jpeg")
       * @return public URL of the uploaded file
       */
      String uploadFile(String key, byte[] data, String contentType);
  }
  ```

### `MinioStorageAdapter.java`
- [ ] Create class in `adapter/out/storage/`:
  - [ ] Annotate with `@Component`
  - [ ] Inject `MinioClient` bean (define it as a `@Bean` in a new `MinioConfig.java` in `infrastructure/config/`)
  - [ ] Inject bucket name from `@Value("${app.minio.bucket}")`
  - [ ] Implement `uploadFile`:
    ```java
    @Override
    public String uploadFile(String key, byte[] data, String contentType) {
        jpaRepository.putObject(PutObjectArgs.builder()
            .bucket(bucket)
            .object(key)
            .stream(new ByteArrayInputStream(data), data.length, -1)
            .contentType(contentType)
            .build());
        return minioClient.getPresignedObjectUrl(...); // or build public URL directly
    }
    ```

### `MinioConfig.java`
- [ ] Create in `infrastructure/config/`:
  ```java
  @Configuration
  public class MinioConfig {
      @Bean
      public MinioClient minioClient(
              @Value("${app.minio.endpoint}") String endpoint,
              @Value("${app.minio.access-key}") String accessKey,
              @Value("${app.minio.secret-key}") String secretKey) {
          return MinioClient.builder()
              .endpoint(endpoint)
              .credentials(accessKey, secretKey)
              .build();
      }
  }
  ```

### `UserController.java` (update)
- [ ] Add `PUT /me/avatar` endpoint:
  ```java
  @PutMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UserResponse>> uploadAvatar(
          @RequestParam("file") MultipartFile file) {
      // 1. Validate MIME type
      // 2. Build storage key: "avatars/" + currentUserId() + "/" + UUID.randomUUID() + ext
      // 3. Call mediaStoragePort.uploadFile(key, file.getBytes(), file.getContentType())
      // 4. Call updateProfileUseCase (or a dedicated UpdateAvatarUseCase) with the URL
      // 5. Return updated UserResponse
  }
  ```
- [ ] Validate content type: throw `400 Bad Request` if not `image/jpeg`, `image/png`, or `image/webp`

### `application.yml` updates
- [ ] Add `spring.servlet.multipart.max-file-size=10MB`
- [ ] Add `spring.servlet.multipart.max-request-size=10MB`
- [ ] Add MinIO properties block
