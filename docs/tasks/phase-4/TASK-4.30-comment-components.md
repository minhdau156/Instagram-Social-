# TASK-4.30 — Comment Components: CommentSection, CommentInput, CommentItem

## Overview

Create the threaded comment UI components embedded inside `PostDetailModal`. This is the most complex UI task in Phase 4 — it must support pagination, nested replies, inline editing, optimistic deletion, and `@mention` autocomplete.

## Requirements

- Lives in `frontend/src/components/comments/`.
- Uses Material UI (MUI) v5 components.
- Threaded: top-level comments shown first; replies loaded on demand per comment.
- Components are focused — `CommentSection` orchestrates, `CommentItem` renders one comment, `CommentInput` handles text entry.

## File Locations

```
frontend/src/components/comments/
├── CommentSection.tsx
├── CommentItem.tsx
└── CommentInput.tsx
```

---

## Checklist

### `CommentSection.tsx`

- [ ] Props interface:
  ```typescript
  interface CommentSectionProps {
    postId: string;
  }
  ```

- [ ] Uses `useComments(postId)` for infinite scroll pagination

- [ ] Renders a `List` of `CommentItem` components (top-level only)

- [ ] Infinite scroll:
  - Add a sentinel `<div ref={sentinelRef} />` at the bottom of the list
  - Use `IntersectionObserver` to call `fetchNextPage()` when sentinel is visible and `hasNextPage` is `true`

- [ ] Show `CircularProgress` at the bottom while `isFetchingNextPage`

- [ ] Show empty state `Typography` (`"No comments yet. Be the first to comment!"`) when total count is 0

- [ ] Show error `Alert` with retry button when query fails

- [ ] Renders `CommentInput` at the bottom of the section (for adding top-level comments)

---

### `CommentItem.tsx`

- [ ] Props interface:
  ```typescript
  interface CommentItemProps {
    comment: Comment;
    postId: string;
    currentUserId?: string;     // for showing edit/delete controls
  }
  ```

- [ ] Renders MUI `ListItem` with:
  - `Avatar` (user avatar, fallback to first letter of `username`)
  - **Username** (bold, clickable — navigates to `/:username`)
  - Comment **content** (or `"[deleted]"` with grey italic style if `status === DELETED`)
  - **Like button** (small `IconButton` with `FavoriteBorderIcon` / `FavoriteIcon`)
  - **Reply button** (small `IconButton` with `ReplyIcon`) that toggles `CommentInput` inline
  - **Relative timestamp** (`"2 hours ago"` using `date-fns` `formatDistanceToNow`)
  - **Edit / Delete menu** (`MoreVertIcon` → MUI `Menu`) — only visible if `comment.userId === currentUserId`

- [ ] Edit flow:
  - Clicking "Edit" replaces the content with an inline `CommentInput` pre-filled with current content
  - On submit: calls `editCommentMutation.mutate({ content })`, then exits edit mode
  - On cancel (Escape key or cancel button): reverts to view mode

- [ ] Delete flow:
  - Clicking "Delete" shows an MUI `Dialog` for confirmation
  - On confirm: calls `deleteCommentMutation.mutate(comment.id)`
  - Use `useQueryClient().invalidateQueries({ queryKey: ['comments', postId] })` after success

- [ ] **Replies section**:
  - Show "View N replies" button when `comment.replyCount > 0`
  - Clicking expands an inline `CommentSection`-like `List` with replies (loaded via `useReplies(comment.id)`)
  - Toggling hides/shows replies (not re-fetched on re-open if cached)
  - Renders inline `CommentInput` for replies when expanded (passes `parentId = comment.id`)

---

### `CommentInput.tsx`

- [ ] Props interface:
  ```typescript
  interface CommentInputProps {
    postId: string;
    parentId?: string | null;   // null or undefined = top-level
    initialValue?: string;      // for edit mode
    onSuccess?: () => void;     // called after successful submit
    onCancel?: () => void;      // called on cancel (edit mode)
    placeholder?: string;
  }
  ```

- [ ] Uses `useAddComment(postId)` or receives an `onSubmit` prop for edit mode

- [ ] Renders MUI `TextField` (multiline, `rows={1}`, `maxRows={4}`):
  - `placeholder` defaults to `"Add a comment…"`
  - Submit on `Enter` key press (without `Shift`)
  - Submit button visible when input is non-empty

- [ ] `@mention` autocomplete:
  - Detect `@` typed in input
  - On `@` trigger: show MUI `Autocomplete` dropdown with matching usernames (search via existing `userApi.ts` or a simple debounced fetch)
  - On selection: insert `@username` into the text at the cursor position

- [ ] Show `CircularProgress` (size 16) in the submit button while mutation is pending

- [ ] Disable submit button when input is empty or mutation is pending

- [ ] On successful submit: clear input field and call `onSuccess?.()` callback

- [ ] Example structure:
  ```tsx
  export function CommentInput({ postId, parentId, initialValue, onSuccess, onCancel, placeholder }: CommentInputProps) {
    const [value, setValue] = useState(initialValue ?? '');
    const addMutation = useAddComment(postId);

    const handleSubmit = () => {
      if (!value.trim()) return;
      addMutation.mutate(
        { content: value.trim(), parentId: parentId ?? null },
        { onSuccess: () => { setValue(''); onSuccess?.(); } }
      );
    };

    return (
      <Box sx={{ display: 'flex', alignItems: 'flex-end', gap: 1, p: 1 }}>
        <TextField
          fullWidth
          multiline
          maxRows={4}
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder={placeholder ?? 'Add a comment…'}
          onKeyDown={(e) => { if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); handleSubmit(); } }}
          size="small"
          variant="standard"
        />
        <IconButton onClick={handleSubmit} disabled={!value.trim() || addMutation.isPending}>
          {addMutation.isPending ? <CircularProgress size={16} /> : <SendIcon />}
        </IconButton>
        {onCancel && (
          <Button size="small" onClick={onCancel}>Cancel</Button>
        )}
      </Box>
    );
  }
  ```
