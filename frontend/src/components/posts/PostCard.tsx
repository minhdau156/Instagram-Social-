import { Card, CardContent, CardActions, Typography, Box, IconButton } from '@mui/material';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { useNavigate } from 'react-router-dom';
import type { PostResponse } from '../../types/post';

interface PostCardProps {
  post: PostResponse;
  onDelete: (post: PostResponse) => void;
}

export default function PostCard({ post, onDelete }: PostCardProps) {
  const navigate = useNavigate();

  return (
    <Card sx={{ cursor: 'pointer' }} onClick={() => navigate(`/posts/${post.id}`)}>
      <CardContent>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          {post.status} · {new Date(post.createdAt).toLocaleDateString()}
        </Typography>

        <Typography variant="body1" sx={{ mb: 1 }}>
          {post.caption || 'No caption'}
        </Typography>

        {post.location && (
          <Typography variant="caption" color="text.secondary">
            📍 {post.location}
          </Typography>
        )}
      </CardContent>

      <CardActions onClick={(e) => e.stopPropagation()}>
        <Typography variant="caption" color="text.secondary">
          ❤️ {post.likeCount} · 💬 {post.commentCount} · 🔖 {post.saveCount}
        </Typography>

        <Box ml="auto">
          <IconButton size="small" onClick={(e) => { e.stopPropagation(); navigate(`/posts/${post.id}`); }}>
            <EditOutlinedIcon fontSize="small" />
          </IconButton>
          <IconButton size="small" onClick={(e) => { e.stopPropagation(); onDelete(post); }}>
            <DeleteOutlineIcon fontSize="small" />
          </IconButton>
        </Box>
      </CardActions>
    </Card>
  );
}
