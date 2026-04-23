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

## ✅ Checklist

- [ ] Create `frontend/src/components/posts/PostCard.tsx`
  - Displays: author avatar + username, carousel of post media (MUI Carousel or Swiper), caption, hashtags, like/comment/save/share action row, timestamp
  - Truncated caption with "more" toggle at 3 lines
- [ ] Create `frontend/src/components/posts/PostDetailModal.tsx`
  - Full-screen dialog: left = media, right = comments panel
- [ ] Create `frontend/src/components/posts/PostGrid.tsx`
  - 3-column `ImageList` with hover overlay (like/comment counts)
  - Clicking a cell opens `PostDetailModal`
