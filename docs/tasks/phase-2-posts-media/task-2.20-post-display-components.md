# TASK-2.20 — Post display components

## 📝 Overview

The goal of this task is to implement the **Post display components** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### UI/UX Requirements
- **Styling:** Use Material UI (MUI) components (`Stack`, `Box`, `Typography`, `Button`).
- **State Management:** Use local state (`useState`) or form state (`react-hook-form`).
- **Responsiveness:** Ensure components work on mobile and desktop by utilizing MUI's `Grid` or responsive props.
- **Error Handling:** Display clear, user-friendly error messages when API calls fail.

## 📂 File Locations

```text
frontend/src/components/posts/PostCard.tsx
frontend/src/components/posts/PostDetailModal.tsx
frontend/src/components/posts/PostGrid.tsx
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```tsx
// frontend/src/components/posts/PostCard.tsx
export const PostCard: React.FC<{ post: Post }> = ({ post }) => {
  const [liked, setLiked] = useState(false);
  const [expanded, setExpanded] = useState(false);

  return (
    <Card sx={{ maxWidth: 600, mb: 2 }}>
      <CardHeader
        avatar={<Avatar>{post.userId[0]}</Avatar>}
        title={<Typography fontWeight="bold">{post.userId}</Typography>}
        subheader={post.location}
        action={<IconButton><MoreVert /></IconButton>}
      />
      {/* Carousel for multiple media */}
      <CardMedia component="img" image={post.mediaItems[0]?.mediaUrl} sx={{ aspectRatio: '1/1', objectFit: 'cover' }} />
      <CardContent>
        {/* Action row */}
        <Stack direction="row" spacing={1}>
          <IconButton onClick={() => setLiked(l => !l)}>
            {liked ? <Favorite color="error" /> : <FavoriteBorder />}
          </IconButton>
          <IconButton><ChatBubbleOutline /></IconButton>
          <IconButton><BookmarkBorder /></IconButton>
        </Stack>
        <Typography fontWeight="bold">{post.likeCount} likes</Typography>
        {/* Truncated caption with toggle */}
        <Typography noWrap={!expanded}>
          {post.caption}
          {!expanded && post.caption && post.caption.length > 100 &&
            <Button size="small" onClick={() => setExpanded(true)}>more</Button>}
        </Typography>
        <Typography variant="caption" color="text.secondary">
          {new Date(post.createdAt).toLocaleDateString()}
        </Typography>
      </CardContent>
    </Card>
  );
};

// frontend/src/components/posts/PostGrid.tsx — 3-column grid for profile
export const PostGrid: React.FC<{ posts: Post[] }> = ({ posts }) => {
  const [selected, setSelected] = useState<Post | null>(null);
  return (
    <>
      <ImageList cols={3} gap={2}>
        {posts.map(post => (
          <ImageListItem key={post.id} onClick={() => setSelected(post)} sx={{ cursor: 'pointer' }}>
            <img src={post.mediaItems[0]?.mediaUrl} style={{ aspectRatio: '1/1', objectFit: 'cover' }} />
          </ImageListItem>
        ))}
      </ImageList>
      {selected && <PostDetailModal post={selected} onClose={() => setSelected(null)} />}
    </>
  );
};
```

## ✅ Checklist

- [x] Create `frontend/src/components/posts/PostCard.tsx`
  - Displays: author avatar + username, carousel of post media (MUI Carousel or Swiper), caption, hashtags, like/comment/save/share action row, timestamp
  - Truncated caption with "more" toggle at 3 lines
- [x] Create `frontend/src/components/posts/PostDetailModal.tsx`
  - Full-screen dialog: left = media, right = comments panel
- [x] Create `frontend/src/components/posts/PostGrid.tsx`
  - 3-column `ImageList` with hover overlay (like/comment counts)
  - Clicking a cell opens `PostDetailModal`
