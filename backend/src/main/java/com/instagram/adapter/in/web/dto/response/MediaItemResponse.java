package com.instagram.adapter.in.web.dto.response;

import java.util.UUID;

import com.instagram.domain.model.PostMedia;

public record MediaItemResponse(
        UUID id,
        String mediaType,
        String mediaUrl,
        String thumbnailUrl,
        Integer width,
        Integer height,
        Double duration,
        Long fileSizeBytes,
        String sortOrder) {
    public static MediaItemResponse from(PostMedia m) {
        return new MediaItemResponse(
                m.getId(),
                m.getMediaType().name(),
                m.getMediaUrl(),
                m.getThumbnailUrl(),
                m.getWidth(),
                m.getHeight(),
                m.getDuration(),
                m.getFileSizeBytes(),
                m.getSortOrder());
    }
}
