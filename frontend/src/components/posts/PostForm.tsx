import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Stack,
  CircularProgress,
  Typography,
  Box,
} from '@mui/material';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';
import { useState, useEffect } from 'react';
import type { PostResponse } from '../../types/post';

interface PostFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: { caption: string; location: string }) => void;
  initialData?: Pick<PostResponse, 'caption' | 'location'> | null;
  isLoading?: boolean;
  mode?: 'create' | 'edit';
}

export default function PostForm({
  open,
  onClose,
  onSubmit,
  initialData,
  isLoading = false,
  mode = 'create',
}: PostFormProps) {
  const [caption,  setCaption]  = useState('');
  const [location, setLocation] = useState('');
  const [errors,   setErrors]   = useState<{ caption?: string }>({});

  useEffect(() => {
    if (open) {
      setCaption(initialData?.caption  ?? '');
      setLocation(initialData?.location ?? '');
      setErrors({});
    }
  }, [open, initialData]);

  const validate = () => {
    const newErrors: typeof errors = {};
    if (caption.length > 2200) {
      newErrors.caption = 'Caption must be at most 2200 characters';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = () => {
    if (!validate()) return;
    onSubmit({ caption, location });
  };

  const isEdit = mode === 'edit';

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        <Box display="flex" alignItems="center" gap={1}>
          <AutoAwesomeIcon sx={{ color: 'primary.main' }} />
          <Typography variant="h6" fontWeight={600}>
            {isEdit ? 'Edit Post' : 'Create New Post'}
          </Typography>
        </Box>
      </DialogTitle>

      <DialogContent dividers sx={{ pt: 2 }}>
        <Stack spacing={2.5}>
          <TextField
            id="post-caption"
            label="Caption"
            placeholder="What's on your mind?"
            multiline
            rows={4}
            fullWidth
            value={caption}
            onChange={(e) => setCaption(e.target.value)}
            error={!!errors.caption}
            helperText={
              errors.caption ?? `${caption.length} / 2200`
            }
            inputProps={{ maxLength: 2200 }}
          />

          <TextField
            id="post-location"
            label="Location"
            placeholder="Add a location…"
            fullWidth
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            inputProps={{ maxLength: 255 }}
          />
        </Stack>
      </DialogContent>

      <DialogActions sx={{ px: 3, py: 2, gap: 1 }}>
        <Button variant="outlined" onClick={onClose} disabled={isLoading}>
          Cancel
        </Button>
        <Button
          variant="contained"
          onClick={handleSubmit}
          disabled={isLoading}
          startIcon={isLoading ? <CircularProgress size={16} color="inherit" /> : undefined}
        >
          {isLoading ? 'Saving…' : isEdit ? 'Save Changes' : 'Create Post'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
