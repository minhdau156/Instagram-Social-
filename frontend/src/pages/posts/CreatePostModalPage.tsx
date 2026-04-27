import { Button } from "@mui/material";
import { useState } from "react";
import { CreatePostModal } from "../../components/posts/CreatePostModal";

export const CreatePostModalPage: React.FC = () => {
    const [open, setOpen] = useState(false);
    return (
        <>
            <Button variant="contained" onClick={() => setOpen(true)}>
                Create Post
            </Button>
            <CreatePostModal open={open} onClose={() => setOpen(false)} />
        </>
    );
};