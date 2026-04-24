package com.instagram.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Hashtag {
    private UUID id;
    private String name;
    private int postCount;
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPostCount() {
        return postCount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Hashtag copy() {
        Hashtag hashtag = new Hashtag();
        hashtag.id = this.id;
        hashtag.name = this.name;
        hashtag.postCount = this.postCount;
        hashtag.createdAt = this.createdAt;
        return hashtag;
    }

    public Hashtag withIncrementedCount() {
        Hashtag hashtag = copy();
        hashtag.postCount++;
        return hashtag;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Hashtag hashtag = new Hashtag();

        public Builder id(UUID id) {
            hashtag.id = id;
            return this;
        }

        public Builder name(String name) {
            hashtag.name = name;
            return this;
        }

        public Builder postCount(int postCount) {
            hashtag.postCount = postCount;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            hashtag.createdAt = createdAt;
            return this;
        }

        public Hashtag build() {
            return hashtag;
        }
    }

}
