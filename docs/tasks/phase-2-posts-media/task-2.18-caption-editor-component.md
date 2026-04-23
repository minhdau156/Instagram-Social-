# TASK-2.18 — Caption editor component

## 📝 Overview

The goal of this task is to implement the **Caption editor component** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### UI/UX Requirements
- **Styling:** Use Material UI (MUI) components (`Stack`, `Box`, `Typography`, `Button`).
- **State Management:** Use local state (`useState`) or form state (`react-hook-form`).
- **Responsiveness:** Ensure components work on mobile and desktop by utilizing MUI's `Grid` or responsive props.
- **Error Handling:** Display clear, user-friendly error messages when API calls fail.

## 📂 File Locations

```text
frontend/src/components/posts/CaptionEditor.tsx
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] Create `frontend/src/components/posts/CaptionEditor.tsx`
  - `<textarea>` with live `#hashtag` highlighting (bold blue) using regex + span injection
  - `@mention` detection with basic user autocomplete dropdown (calls `/api/v1/search?q=&type=users`)
  - Character counter (max 2200)
