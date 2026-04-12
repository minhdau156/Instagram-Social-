import type {
  CreatePostRequest,
  PagedResponse,
  PostResponse,
  UpdatePostRequest,
} from '../types/post';
import api from './axios';

const BASE = '/api/v1/posts';

export const postApi = {
  /** GET /api/v1/posts?page=&size= */
  listPosts: async (page = 0, size = 20): Promise<PagedResponse<PostResponse>> => {
    const { data } = await api.get<PagedResponse<PostResponse>>(
      `${BASE}?page=${page}&size=${size}`,
    );
    return data;
  },

  /** GET /api/v1/posts?userId=&page=&size= */
  listPostsByUser: async (
    userId: string,
    page = 0,
    size = 20,
  ): Promise<PagedResponse<PostResponse>> => {
    const { data } = await api.get<PagedResponse<PostResponse>>(
      `${BASE}?userId=${userId}&page=${page}&size=${size}`,
    );
    return data;
  },

  /** GET /api/v1/posts/:id */
  getPost: async (id: string): Promise<PostResponse> => {
    const { data } = await api.get<PostResponse>(`${BASE}/${id}`);
    return data;
  },

  /** POST /api/v1/posts */
  createPost: async (body: CreatePostRequest): Promise<PostResponse> => {
    const { data } = await api.post<PostResponse>(BASE, body);
    return data;
  },

  /** PUT /api/v1/posts/:id */
  updatePost: async (id: string, body: UpdatePostRequest): Promise<PostResponse> => {
    const { data } = await api.put<PostResponse>(`${BASE}/${id}`, body);
    return data;
  },

  /** DELETE /api/v1/posts/:id */
  deletePost: async (id: string): Promise<void> => {
    await api.delete(`${BASE}/${id}`);
  },
};
