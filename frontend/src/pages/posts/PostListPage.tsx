import {
  Box,
  Button,
  Fab,
  Grid,
  Snackbar,
  Alert,
  Typography,
  Skeleton,
  alpha,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import GridViewIcon from '@mui/icons-material/GridView';
import { useState } from 'react';
import PostCard from '../../components/posts/PostCard';
import PostForm from '../../components/posts/PostForm';
import PostDeleteDialog from '../../components/posts/PostDeleteDialog';
import { usePosts, useCreatePost, useDeletePost } from '../../hooks/usePosts';
import type { PostResponse } from '../../types/post';

export default function PostListPage() {
  const [page] = useState(0);
  const { data, isLoading, isError } = usePosts(page, 20);

  const createPost = useCreatePost();
  const deletePost = useDeletePost();

  const [createOpen, setCreateOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<PostResponse | null>(null);
  const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
    open: false, message: '', severity: 'success',
  });

  const handleCreate = async (data: { caption: string; location: string }) => {
    try {
      await createPost.mutateAsync(data);
      setCreateOpen(false);
      setSnackbar({ open: true, message: 'Post created successfully!', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to create post.', severity: 'error' });
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await deletePost.mutateAsync(deleteTarget.id);
      setDeleteTarget(null);
      setSnackbar({ open: true, message: 'Post deleted.', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to delete post.', severity: 'error' });
    }
  };

  return (
    <Box sx={{ p: { xs: 2, md: 4 }, minHeight: '100vh' }}>
      {/* ── Header ── */}
      <Box
        display="flex"
        alignItems="center"
        justifyContent="space-between"
        mb={4}
        sx={{
          background: (t) =>
            `linear-gradient(135deg, ${alpha(t.palette.primary.main, 0.12)} 0%, ${alpha(t.palette.secondary.main, 0.06)} 100%)`,
          borderRadius: 3,
          p: 3,
          border: (t) => `1px solid ${alpha(t.palette.primary.main, 0.2)}`,
        }}
      >
        <Box>
          <Box display="flex" alignItems="center" gap={1.5} mb={0.5}>
            <GridViewIcon sx={{ color: 'primary.main' }} />
            <Typography variant="h4" fontWeight={700}>
              Posts
            </Typography>
          </Box>
          <Typography variant="body2" color="text.secondary">
            {data ? `${data.totalInPage} posts on this page` : 'Loading…'}
          </Typography>
        </Box>

        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => setCreateOpen(true)}
          size="large"
        >
          New Post
        </Button>
      </Box>

      {/* ── Error ── */}
      {isError && (
        <Alert severity="error" sx={{ mb: 3 }}>
          Failed to load posts. Make sure the backend is running on port 8080.
        </Alert>
      )}

      {/* ── Grid ── */}
      <Grid container spacing={3}>
        {isLoading
          ? Array.from({ length: 6 }).map((_, i) => (
            <Grid key={i} item xs={12} sm={6} md={4}>
              <Skeleton variant="rounded" height={260} sx={{ borderRadius: 3 }} />
            </Grid>
          ))
          : data?.content?.map((post) => (
            <Grid key={post.id} item xs={12} sm={6} md={4}>
              <PostCard post={post} onDelete={setDeleteTarget} />
            </Grid>
          ))}
      </Grid>

      {/* ── Empty state ── */}
      {!isLoading && data?.content?.length === 0 && (
        <Box textAlign="center" py={10}>
          <Typography variant="h6" color="text.secondary" mb={2}>
            No posts yet
          </Typography>
          <Button variant="contained" startIcon={<AddIcon />} onClick={() => setCreateOpen(true)}>
            Create your first post
          </Button>
        </Box>
      )}

      {/* ── FAB (mobile) ── */}
      <Fab
        color="primary"
        sx={{
          position: 'fixed',
          bottom: 32,
          right: 32,
          display: { sm: 'none' },
          background: 'linear-gradient(135deg, #8B5CF6, #EC4899)',
        }}
        onClick={() => setCreateOpen(true)}
      >
        <AddIcon />
      </Fab>

      {/* ── Dialogs ── */}
      <PostForm
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        onSubmit={handleCreate}
        isLoading={createPost.isPending}
        mode="create"
      />

      <PostDeleteDialog
        open={!!deleteTarget}
        post={deleteTarget}
        isLoading={deletePost.isPending}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
      />

      {/* ── Snackbar ── */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3500}
        onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} variant="filled" onClose={() => setSnackbar((s) => ({ ...s, open: false }))}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
