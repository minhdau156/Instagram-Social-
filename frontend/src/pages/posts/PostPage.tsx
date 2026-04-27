import { CircularProgress, Typography, Container } from "@mui/material";
import { useParams } from "react-router-dom";
import { PostCard } from "../../components/posts/PostCard";
import { usePost } from "../../hooks/post/usePost";
import { CreatePostModal } from "../../components/posts/CreatePostModal";
import { useState } from "react";

export const PostPage: React.FC = () => {
    const { postId } = useParams<{ postId: string }>();
    const { data: post, isLoading, isError } = usePost(postId!);
    const [open, setOpen] = useState(false);

    if (isLoading) return <CircularProgress />;
    if (isError || !post) return <Typography>Post not found.</Typography>;

    return (<>
        <Container maxWidth="sm" sx={{ py: 4 }}>
            <PostCard post={post} />
        </Container>

    </>);
};