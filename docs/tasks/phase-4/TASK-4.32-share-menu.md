# TASK-4.32 — Share Menu Component

## Overview

Create the `ShareMenu` component — a dropdown menu triggered by a share icon button on posts. It provides options to copy the post link or send the post as a DM (placeholder).

## Requirements

- Lives in `frontend/src/components/posts/`.
- Uses Material UI (MUI) v5 `Menu`, `MenuItem`, and `IconButton`.
- `Copy Link` is fully functional (uses the Clipboard API).
- `Send as Message` calls the share API with `shareType: 'DM'` but the actual DM flow is a Phase 7 concern — show a toast/snackbar acknowledging the action for now.

## File Location

```
frontend/src/components/posts/ShareMenu.tsx
```

---

## Checklist

- [ ] Props interface:
  ```typescript
  interface ShareMenuProps {
    postId: string;
    disabled?: boolean;    // disable for unauthenticated users (send as message)
  }
  ```

- [ ] State: `anchorEl: HTMLElement | null` for menu open/close

- [ ] Renders trigger `IconButton` with `ShareIcon` or `IosShareIcon` from `@mui/icons-material`

- [ ] MUI `Menu` anchored to the trigger button with two `MenuItem` options:

  #### Option 1: Copy Link
  ```tsx
  <MenuItem onClick={handleCopyLink}>
    <LinkIcon sx={{ mr: 1 }} />
    Copy Link
  </MenuItem>
  ```
  - `handleCopyLink` uses:
    ```typescript
    const handleCopyLink = async () => {
      const url = `${window.location.origin}/p/${postId}`;
      await navigator.clipboard.writeText(url);
      // Show success snackbar/toast: "Link copied to clipboard!"
      handleClose();
    };
    ```
  - Show MUI `Snackbar` with `Alert` (severity `"success"`) for 2 seconds after copying

  #### Option 2: Send as Message
  ```tsx
  <MenuItem onClick={handleShare} disabled={disabled}>
    <SendIcon sx={{ mr: 1 }} />
    Send as Message
  </MenuItem>
  ```
  - `handleShare` calls `sharePost(postId, { shareType: 'DM' })` (from `sharesApi`)
  - On success: show snackbar `"Post shared! (DM delivery coming soon)"`
  - On error: show error snackbar

- [ ] Manage snackbar state:
  ```typescript
  const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
    open: false, message: '', severity: 'success',
  });
  ```

- [ ] Close menu on any option selection (`handleClose` sets `anchorEl = null`)

- [ ] Add `aria-label="Share post"` on the trigger button

- [ ] Export `ShareMenu` as a named export
