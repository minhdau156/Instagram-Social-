package com.instagram.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PostMedia {
    private UUID id;
    private UUID postId;
    private String mediaUrl;
    private MediaType mediaType;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Double duration;
    private Long fileSizeBytes;
    private int sortOrder;
    private OffsetDateTime createdAt;

    // getters
    public UUID getId() {
        return id;
    }

    public UUID getPostId() {
        return postId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getDuration() {
        return duration;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PostMedia postMedia = new PostMedia();

        public Builder id(UUID id) {
            postMedia.id = id;
            return this;
        }

        public Builder postId(UUID postId) {
            postMedia.postId = postId;
            return this;
        }

        public Builder mediaUrl(String mediaUrl) {
            postMedia.mediaUrl = mediaUrl;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            postMedia.mediaType = mediaType;
            return this;
        }

        public Builder thumbnailUrl(String thumbnailUrl) {
            postMedia.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder width(Integer width) {
            postMedia.width = width;
            return this;
        }

        public Builder height(Integer height) {
            postMedia.height = height;
            return this;
        }

        public Builder duration(Double duration) {
            postMedia.duration = duration;
            return this;
        }

        public Builder fileSizeBytes(Long fileSizeBytes) {
            postMedia.fileSizeBytes = fileSizeBytes;
            return this;
        }

        public Builder sortOrder(int sortOrder) {
            postMedia.sortOrder = sortOrder;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            postMedia.createdAt = createdAt;
            return this;
        }

        public PostMedia build() {
            return postMedia;
        }
    }

}
