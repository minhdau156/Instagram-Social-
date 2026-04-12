/** Mirror of the backend PostStatus enum */
export type PostStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED' | 'DELETED';

/** Response shape returned by GET /api/v1/posts and GET /api/v1/posts/:id */
export interface PostResponse {
  id: string;
  userId: string;
  caption: string | null;
  location: string | null;
  status: PostStatus;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  saveCount: number;
  shareCount: number;
  createdAt: string;  // ISO-8601 OffsetDateTime
  updatedAt: string;
}

/** Request body for POST /api/v1/posts */
export interface CreatePostRequest {
  caption?: string;
  location?: string;
}

/** Request body for PUT /api/v1/posts/:id */
export interface UpdatePostRequest {
  caption?: string;
  location?: string;
}

/** Generic paginated response wrapper */
export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalInPage: number;
}
