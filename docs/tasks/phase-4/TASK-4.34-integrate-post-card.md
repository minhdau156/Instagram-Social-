# TASK-4.34 — Integrate Action Components into PostCard

## Overview

Update `PostCard.tsx` and `PostDetailModal.tsx` to include all the social interaction components created in TASK-4.29 through TASK-4.32. After this task, users can like, comment, save, and share posts directly from the feed and post detail view.

## Requirements

- Updates two existing files — no new files.
- Do NOT break existing PostCard functionality (caption, images, header, navigation).
- The action bar layout should match Instagram's layout: like, comment, share on the left; save on the right.
- Only show like/comment/save/share controls to authenticated users (use `useAuth()`).

## File Locations

```
frontend/src/components/posts/PostCard.tsx        ← UPDATE
frontend/src/components/posts/PostDetailModal.tsx ← UPDATE
```

---

## Checklist

### `PostCard.tsx` Updates

- [ ] Import:
  ```typescript
  import { LikeButton } from './LikeButton';
  import { LikersTooltip } from './LikersTooltip';
  import { SaveButton } from './SaveButton';
  import { ShareMenu } from './ShareMenu';
  import { useAuth } from '../../hooks/useAuth';
  ```

- [ ] Get current auth state: `const { user } = useAuth();`

- [ ] Add an **Action Bar** section between the post image and the caption:
  ```tsx
  {/* Action Bar */}
  <Box sx={{ display: 'flex', alignItems: 'center', px: 1, py: 0.5 }}>
    {/* Left: Like, Comment trigger, Share */}
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
      <LikeButton
        postId={post.id}
        liked={post.likedByCurrentUser ?? false}
        likeCount={post.likeCount}
        disabled={!user}
      />
      <IconButton
        size="small"
        aria-label="Comment on post"
        onClick={handleOpenDetail}   // opens PostDetailModal
      >
        <ChatBubbleOutlineIcon />
      </IconButton>
      <ShareMenu postId={post.id} disabled={!user} />
    </Box>

    {/* Spacer */}
    <Box sx={{ flexGrow: 1 }} />

    {/* Right: Save */}
    <SaveButton
      postId={post.id}
      saved={post.savedByCurrentUser ?? false}
      disabled={!user}
    />
  </Box>
  ```

- [ ] Below the action bar, add **LikersTooltip**:
  ```tsx
  {post.likeCount > 0 && (
    <LikersTooltip
      postId={post.id}
      likeCount={post.likeCount}
    />
  )}
  ```

- [ ] Add a comment count trigger below the likers line:
  ```tsx
  {post.commentCount > 0 && (
    <Typography
      variant="body2"
      color="text.secondary"
      sx={{ px: 2, cursor: 'pointer' }}
      onClick={handleOpenDetail}
    >
      View all {post.commentCount} comments
    </Typography>
  )}
  ```

- [ ] Verify the `Post` type includes `likeCount`, `commentCount`, `likedByCurrentUser`, `savedByCurrentUser` (updated in TASK-4.26)

---

### `PostDetailModal.tsx` Updates

- [ ] Embed `CommentSection` at the bottom of the modal content area:
  ```tsx
  import { CommentSection } from '../comments/CommentSection';

  // Inside the modal content:
  <Divider />
  <CommentSection postId={post.id} />
  ```

- [ ] Add the same **Action Bar** as `PostCard` inside the modal (above the `CommentSection`):
  - Ensures like/save/share actions work from the detail modal too

- [ ] Ensure the modal has sufficient `maxHeight` and `overflow: 'auto'` so the comment list scrolls within the modal without the whole page scrolling

- [ ] If a comment trigger opens the modal from the feed, auto-focus the `CommentInput` at the bottom:
  - Pass an `autoFocusComment?: boolean` prop to `PostDetailModal`
  - When `true`, call `inputRef.current?.focus()` after the modal opens
