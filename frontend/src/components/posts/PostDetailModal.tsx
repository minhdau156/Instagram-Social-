import React from 'react';
import { Dialog, Box, Typography, IconButton, Avatar, Stack, Divider } from '@mui/material';
import { Close, FavoriteBorder, ChatBubbleOutline, BookmarkBorder } from '@mui/icons-material';
import { Post } from '../../types/post';

interface PostDetailModalProps {
    post: Post;
    onClose: () => void;
}

export const PostDetailModal: React.FC<PostDetailModalProps> = ({ post, onClose }) => {
    return (
        <Dialog open={true} onClose={onClose} fullScreen>
            <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, height: '100vh' }}>
                {/* Left Side - Media */}
                <Box sx={{ 
                    flex: { xs: 'none', md: '1 1 60%' }, 
                    bgcolor: 'black', 
                    display: 'flex', 
                    alignItems: 'center', 
                    justifyContent: 'center',
                    borderRight: 1,
                    borderColor: 'divider'
                }}>
                    <img 
                        src={post.mediaItems?.[0]?.mediaUrl || ''} 
                        alt="Post media" 
                        style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }} 
                    />
                </Box>
                
                {/* Right Side - Details & Comments */}
                <Box sx={{ 
                    flex: { xs: 'none', md: '1 1 40%' }, 
                    display: 'flex', 
                    flexDirection: 'column',
                    bgcolor: 'background.paper'
                }}>
                    {/* Header */}
                    <Box sx={{ p: 2, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                        <Stack direction="row" spacing={2} alignItems="center">
                            <Avatar>{String(post.userId).charAt(0)}</Avatar>
                            <Typography fontWeight="bold">{post.userId}</Typography>
                        </Stack>
                        <IconButton onClick={onClose}>
                            <Close />
                        </IconButton>
                    </Box>
                    <Divider />
                    
                    {/* Comments Area (Caption + Comments) */}
                    <Box sx={{ p: 2, flexGrow: 1, overflowY: 'auto' }}>
                        <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
                            <Avatar>{String(post.userId).charAt(0)}</Avatar>
                            <Box>
                                <Typography component="span" fontWeight="bold" sx={{ mr: 1 }}>
                                    {post.userId}
                                </Typography>
                                <Typography component="span" sx={{ whiteSpace: 'pre-wrap' }}>
                                    {post.caption}
                                </Typography>
                                <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 0.5 }}>
                                    {new Date(post.createdAt).toLocaleDateString()}
                                </Typography>
                            </Box>
                        </Stack>
                        <Typography color="text.secondary" variant="body2" sx={{ textAlign: 'center', mt: 4 }}>
                            No comments yet.
                        </Typography>
                    </Box>
                    <Divider />
                    
                    {/* Action Row */}
                    <Box sx={{ p: 2 }}>
                        <Stack direction="row" spacing={1} sx={{ mb: 1 }}>
                            <IconButton><FavoriteBorder /></IconButton>
                            <IconButton><ChatBubbleOutline /></IconButton>
                            <IconButton><BookmarkBorder /></IconButton>
                        </Stack>
                        <Typography fontWeight="bold" sx={{ mb: 1 }}>{post.likeCount} likes</Typography>
                        <Typography variant="caption" color="text.secondary">
                            {new Date(post.createdAt).toLocaleDateString()}
                        </Typography>
                    </Box>
                </Box>
            </Box>
        </Dialog>
    );
};
