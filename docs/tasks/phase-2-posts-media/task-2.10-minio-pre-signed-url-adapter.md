# TASK-2.10 — MinIO pre-signed URL adapter

## 📝 Overview

The goal of this task is to implement the **MinIO pre-signed URL adapter** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### Adapter Responsibilities
- Implement the Out-Port interface.
- Inject the corresponding Spring Data `JpaRepository`.
- Provide private `toEntity(DomainModel)` and `toDomain(JpaEntity)` mapper methods.
- Add `@Component` or `@Repository` to register it as a Spring Bean.

## 📂 File Locations

```text
backend/src/main/java/com/instagram/infrastructure/persistence/adapter/MinioStorageAdapter.java
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```java
// MediaStoragePort.java (out-port interface)
public interface MediaStoragePort {
    String generatePresignedPutUrl(String key, Duration expiry);
}

// MinioStorageAdapter.java
@Component
public class MinioStorageAdapter implements MediaStoragePort {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageAdapter(MinioClient minioClient,
                               @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public String generatePresignedPutUrl(String key, Duration expiry) {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(key)
                .expiry((int) expiry.toSeconds(), TimeUnit.SECONDS)
                .build()
        );
    }
}
```

## ✅ Checklist

- [ ] Update `MinioStorageAdapter.java` (from Phase 1 TASK-1.15) to add:
  - `generatePresignedPutUrl(String key, Duration expiry) → String`
- [ ] Ensure `MediaStoragePort` interface includes `generatePresignedPutUrl`
