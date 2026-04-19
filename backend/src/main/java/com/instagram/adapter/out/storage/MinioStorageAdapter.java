package com.instagram.adapter.out.storage;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.instagram.domain.port.out.MediaStoragePort;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Component
public class MinioStorageAdapter implements MediaStoragePort {

    private final MinioClient minioClient;
    private final String bucket;
    private final String endpoint;

    public MinioStorageAdapter(MinioClient minioClient, 
            @Value("${app.minio.bucket}") String bucket,
            @Value("${app.minio.endpoint}") String endpoint) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        this.endpoint = endpoint;
    }

    @Override
    public String uploadFile(String key, byte[] data, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(new ByteArrayInputStream(data), data.length, -1)
                    .contentType(contentType)
                    .build());
            return String.format("%s/%s/%s", endpoint, bucket, key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }
}
