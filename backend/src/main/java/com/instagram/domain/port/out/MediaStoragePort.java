package com.instagram.domain.port.out;

public interface MediaStoragePort {
    String uploadFile(String key, byte[] data, String contentType);
}
