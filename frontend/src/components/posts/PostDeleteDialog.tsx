import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box,
  CircularProgress,
} from '@mui/material';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import type { PostResponse } from '../../types/post';

interface PostDeleteDialogProps {
  open: boolean;
  post: PostResponse | null;
  isLoading?: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export default function PostDeleteDialog({
  open,
  post,
  isLoading = false,
  onClose,
  onConfirm,
}: PostDeleteDialogProps) {
  const preview = post?.caption
    ? `"${post.caption.slice(0, 60)}${post.caption.length > 60 ? '…' : ''}"`
    : 'this post';

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>
        <Box display="flex" alignItems="center" gap={1.5}>
          <WarningAmberIcon sx={{ color: 'error.main', fontSize: 28 }} />
          <Typography variant="h6" fontWeight={600}>
            Delete Post
          </Typography>
        </Box>
      </DialogTitle>

      <DialogContent>
        <Typography color="text.secondary" sx={{ lineHeight: 1.7 }}>
          Are you sure you want to delete {preview}? This action will soft-delete the
          post — it will no longer appear publicly but can be reviewed by admins.
        </Typography>
      </DialogContent>

      <DialogActions sx={{ px: 3, py: 2, gap: 1 }}>
        <Button variant="outlined" onClick={onClose} disabled={isLoading}>
          Cancel
        </Button>
        <Button
          variant="contained"
          color="error"
          onClick={onConfirm}
          disabled={isLoading}
          startIcon={isLoading ? <CircularProgress size={16} color="inherit" /> : undefined}
          sx={{ background: 'linear-gradient(135deg, #EF4444 0%, #DC2626 100%)' }}
        >
          {isLoading ? 'Deleting…' : 'Delete'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
