import {
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { postApi } from '../api/postApi';
import type { CreatePostRequest, UpdatePostRequest } from '../types/post';

// ── Query keys ────────────────────────────────────────────────────────────── //
export const postKeys = {
  all:    ['posts'] as const,
  lists:  () => [...postKeys.all, 'list'] as const,
  list:   (page: number, size: number) => [...postKeys.lists(), { page, size }] as const,
  detail: (id: string) => [...postKeys.all, 'detail', id] as const,
};

// ── List all posts ────────────────────────────────────────────────────────── //
export const usePosts = (page = 0, size = 20) =>
  useQuery({
    queryKey: postKeys.list(page, size),
    queryFn: () => postApi.listPosts(page, size),
    staleTime: 30_000,
  });

// ── Get single post ───────────────────────────────────────────────────────── //
export const usePost = (id: string) =>
  useQuery({
    queryKey: postKeys.detail(id),
    queryFn: () => postApi.getPost(id),
    enabled: !!id,
    staleTime: 30_000,
  });

// ── Create post ───────────────────────────────────────────────────────────── //
export const useCreatePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (body: CreatePostRequest) => postApi.createPost(body),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: postKeys.lists() });
    },
  });
};

// ── Update post ───────────────────────────────────────────────────────────── //
export const useUpdatePost = (id: string) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (body: UpdatePostRequest) => postApi.updatePost(id, body),
    onSuccess: (updated) => {
      queryClient.setQueryData(postKeys.detail(id), updated);
      queryClient.invalidateQueries({ queryKey: postKeys.lists() });
    },
  });
};

// ── Delete post ───────────────────────────────────────────────────────────── //
export const useDeletePost = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => postApi.deletePost(id),
    onSuccess: (_data, id) => {
      queryClient.removeQueries({ queryKey: postKeys.detail(id) });
      queryClient.invalidateQueries({ queryKey: postKeys.lists() });
    },
  });
};
