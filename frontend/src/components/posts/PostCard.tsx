import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Chip,
  Box,
  IconButton,
  Tooltip,
  alpha,
} from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline';
import BookmarkBorderIcon from '@mui/icons-material/BookmarkBorder';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import LocationOnOutlinedIcon from '@mui/icons-material/LocationOnOutlined';
import { useNavigate } from 'react-router-dom';
import type { PostResponse } from '../../types/post';

interface PostCardProps {
  post: PostResponse;
  onDelete: (post: PostResponse) => void;
}

const statusColors: Record<string, 'success' | 'warning' | 'default' | 'error'> = {
  PUBLISHED: 'success',
  DRAFT:     'warning',
  ARCHIVED:  'default',
  DELETED:   'error',
};

export default function PostCard({ post, onDelete }: PostCardProps) {
  const navigate = useNavigate();

  const formattedDate = new Date(post.createdAt).toLocaleDateString('en-US', {
    year: 'numeric', month: 'short', day: 'numeric',
  });

  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        cursor: 'pointer',
      }}
      onClick={() => navigate(`/posts/${post.id}`)}
    >
      {/* Gradient banner */}
      <Box
        sx={{
          height: 6,
          background: 'linear-gradient(90deg, #8B5CF6 0%, #EC4899 100%)',
        }}
      />

      <CardContent sx={{ flex: 1, pt: 2 }}>
        {/* Header row */}
        <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={1}>
          <Chip
            label={post.status}
            color={statusColors[post.status] ?? 'default'}
            size="small"
            sx={{ fontWeight: 600, fontSize: '0.7rem' }}
          />
          <Typography variant="caption" color="text.secondary">
            {formattedDate}
          </Typography>
        </Box>

        {/* Caption */}
        <Typography
          variant="body1"
          sx={{
            mt: 1,
            mb: 1.5,
            display: '-webkit-box',
            WebkitLineClamp: 3,
            WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
            lineHeight: 1.6,
            minHeight: 72,
            color: post.caption ? 'text.primary' : 'text.secondary',
            fontStyle: post.caption ? 'normal' : 'italic',
          }}
        >
          {post.caption || 'No caption'}
        </Typography>

        {/* Location */}
        {post.location && (
          <Box display="flex" alignItems="center" gap={0.5} mb={1}>
            <LocationOnOutlinedIcon sx={{ fontSize: 14, color: 'secondary.main' }} />
            <Typography variant="caption" color="text.secondary">
              {post.location}
            </Typography>
          </Box>
        )}
      </CardContent>

      {/* Stats + Actions */}
      <CardActions
        sx={{
          px: 2,
          pb: 1.5,
          borderTop: (t) => `1px solid ${alpha(t.palette.divider, 1)}`,
          justifyContent: 'space-between',
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Engagement stats */}
        <Box display="flex" gap={1.5}>
          <Box display="flex" alignItems="center" gap={0.5}>
            <FavoriteIcon sx={{ fontSize: 15, color: '#F87171' }} />
            <Typography variant="caption" color="text.secondary">{post.likeCount}</Typography>
          </Box>
          <Box display="flex" alignItems="center" gap={0.5}>
            <ChatBubbleOutlineIcon sx={{ fontSize: 15, color: 'info.main' }} />
            <Typography variant="caption" color="text.secondary">{post.commentCount}</Typography>
          </Box>
          <Box display="flex" alignItems="center" gap={0.5}>
            <BookmarkBorderIcon sx={{ fontSize: 15, color: 'success.main' }} />
            <Typography variant="caption" color="text.secondary">{post.saveCount}</Typography>
          </Box>
        </Box>

        {/* Action buttons */}
        <Box>
          <Tooltip title="Edit">
            <IconButton
              size="small"
              sx={{ color: 'primary.main' }}
              onClick={(e) => { e.stopPropagation(); navigate(`/posts/${post.id}`); }}
            >
              <EditOutlinedIcon fontSize="small" />
            </IconButton>
          </Tooltip>
          <Tooltip title="Delete">
            <IconButton
              size="small"
              sx={{ color: 'error.main' }}
              onClick={(e) => { e.stopPropagation(); onDelete(post); }}
            >
              <DeleteOutlineIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Box>
      </CardActions>
    </Card>
  );
}
