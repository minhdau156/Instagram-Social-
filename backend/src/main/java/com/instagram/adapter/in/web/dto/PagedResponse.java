package com.instagram.adapter.in.web.dto;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param <T> the type of content items
 */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        int totalInPage
) {
    public static <T> PagedResponse<T> of(List<T> content, int page, int size) {
        return new PagedResponse<>(content, page, size, content.size());
    }
}
