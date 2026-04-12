import {
  Box,
  Button,
  Chip,
  CircularProgress,
  Divider,
  Grid,
  Paper,
  Snackbar,
  Alert,
  Typography,
  alpha,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';
import BookmarkBorderIcon from '@mui/icons-material/BookmarkBorder';
import ShareOutlinedIcon from '@mui/icons-material/ShareOutlined';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import LocationOnOutlinedIcon from '@mui/icons-material/LocationOnOutlined';
import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import PostForm from '../../components/posts/PostForm';
import PostDeleteDialog from '../../components/posts/PostDeleteDialog';
import { useDeletePost, usePost, useUpdatePost } from '../../hooks/usePosts';

const statusColors: Record<string, 'success' | 'warning' | 'default' | 'error'> = {
  PUBLISHED: 'success',
  DRAFT:     'warning',
  ARCHIVED:  'default',
  DELETED:   'error',
};

export default function PostDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data: post, isLoading, isError } = usePost(id!);
  const updatePost = useUpdatePost(id!);
  const deletePost = useDeletePost();

  const [editOpen,   setEditOpen]   = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
    open: false, message: '', severity: 'success',
  });

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !post) {
    return (
      <Box p={4}>
        <Alert severity="error" sx={{ mb: 2 }}>
          Post not found or failed to load.
        </Alert>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/posts')}>
          Back to Posts
        </Button>
      </Box>
    );
  }

  const handleUpdate = async (data: { caption: string; location: string }) => {
    try {
      await updatePost.mutateAsync(data);
      setEditOpen(false);
      setSnackbar({ open: true, message: 'Post updated!', severity: 'success' });
    } catch {
      setSnackbar({ open: true, message: 'Failed to update post.', severity: 'error' });
    }
  };

  const handleDelete = async () => {
    try {
      await deletePost.mutateAsync(post.id);
      navigate('/posts');
    } catch {
      setSnackbar({ open: true, message: 'Failed to delete post.', severity: 'error' });
      setDeleteOpen(false);
    }
  };

  const formattedDate = new Date(post.createdAt).toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit',
  });

  return (
    <Box sx={{ p: { xs: 2, md: 4 }, maxWidth: 900, mx: 'auto' }}>
      {/* Navigation */}
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/posts')}
        sx={{ mb: 3 }}
      >
        Back to Posts
      </Button>

      <Paper
        elevation={0}
        sx={{
          border: (t) => `1px solid ${alpha(t.palette.divider, 1)}`,
          borderRadius: 3,
          overflow: 'hidden',
        }}
      >
        {/* Gradient top band */}
        <Box sx={{ height: 8, background: 'linear-gradient(90deg, #8B5CF6 0%, #EC4899 100%)' }} />

        <Box sx={{ p: { xs: 2, md: 4 } }}>
          {/* Header */}
          <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={3} flexWrap="wrap" gap={2}>
            <Box>
              <Chip
                label={post.status}
                color={statusColors[post.status] ?? 'default'}
                size="small"
                sx={{ mb: 1, fontWeight: 600 }}
              />
              <Typography variant="caption" display="block" color="text.secondary">
                {formattedDate}
              </Typography>
              <Typography variant="caption" color="text.disabled" sx={{ fontFamily: 'monospace', fontSize: '0.7rem' }}>
                ID: {post.id}
              </Typography>
            </Box>

            {/* Actions */}
            <Box display="flex" gap={1}>
              <Button
                variant="outlined"
                startIcon={<EditOutlinedIcon />}
                onClick={() => setEditOpen(true)}
                size="small"
              >
                Edit
              </Button>
              <Button
                variant="outlined"
                color="error"
                startIcon={<DeleteOutlineIcon />}
                onClick={() => setDeleteOpen(true)}
                size="small"
              >
                Delete
              </Button>
            </Box>
          </Box>

          <Divider sx={{ mb: 3 }} />

          {/* Caption */}
          <Typography
            variant="body1"
            sx={{ lineHeight: 1.8, mb: 2, whiteSpace: 'pre-wrap', minHeight: 80 }}
            color={post.caption ? 'text.primary' : 'text.secondary'}
          >
            {post.caption || 'No caption provided.'}
          </Typography>

          {/* Location */}
          {post.location && (
            <Box display="flex" alignItems="center" gap={0.5} mb={3}>
              <LocationOnOutlinedIcon sx={{ fontSize: 16, color: 'secondary.main' }} />
              <Typography variant="body2" color="text.secondary">{post.location}</Typography>
            </Box>
          )}

          <Divider sx={{ mb: 3 }} />

          {/* Stats grid */}
          <Grid container spacing={2}>
            {[
              { icon: <VisibilityOutlinedIcon />, label: 'Views',    value: post.viewCount,    color: 'text.secondary' },
              { icon: <FavoriteIcon />,           label: 'Likes',    value: post.likeCount,    color: '#F87171' },
              { icon: <ChatBubbleOutlineIcon />,  label: 'Comments', value: post.commentCount, color: 'info.main' },
              { icon: <BookmarkBorderIcon />,     label: 'Saves',    value: post.saveCount,    color: 'success.main' },
              { icon: <ShareOutlinedIcon />,      label: 'Shares',   value: post.shareCount,   color: 'secondary.main' },
            ].map(({ icon, label, value, color }) => (
              <Grid key={label} size={{ xs: 6, sm: 4, md: 'auto' }}>
                <Box
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: 0.5,
                    px: 2,
                    py: 1.5,
                    borderRadius: 2,
                    background: (t) => alpha(t.palette.background.default, 0.5),
                    border: (t) => `1px solid ${alpha(t.palette.divider, 1)}`,
                    minWidth: 80,
                  }}
                >
                  <Box sx={{ color }}>{icon}</Box>
                  <Typography variant="h6" fontWeight={700} lineHeight={1}>
                    {value.toLocaleString()}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">{label}</Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
        </Box>
      </Paper>

      {/* Dialogs */}
      <PostForm
        open={editOpen}
        onClose={() => setEditOpen(false)}
        onSubmit={handleUpdate}
        initialData={{ caption: post.caption, location: post.location }}
        isLoading={updatePost.isPending}
        mode="edit"
      />

      <PostDeleteDialog
        open={deleteOpen}
        post={post}
        isLoading={deletePost.isPending}
        onClose={() => setDeleteOpen(false)}
        onConfirm={handleDelete}
      />

      <Snackbar
        open={snackbar.open}
        autoHideDuration={3500}
        onClose={() => setSnackbar((s) => ({ ...s, open: false }))}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} variant="filled">
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
