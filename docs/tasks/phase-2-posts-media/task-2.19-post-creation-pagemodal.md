# TASK-2.19 — Post creation page/modal

## 📝 Overview

The goal of this task is to implement the **Post creation page/modal** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### UI/UX Requirements
- **Styling:** Use Material UI (MUI) components (`Stack`, `Box`, `Typography`, `Button`).
- **State Management:** Use local state (`useState`) or form state (`react-hook-form`).
- **Responsiveness:** Ensure components work on mobile and desktop by utilizing MUI's `Grid` or responsive props.
- **Error Handling:** Display clear, user-friendly error messages when API calls fail.

## 📂 File Locations

```text
frontend/src/components/posts/CreatePostModal.tsx
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] Create `frontend/src/components/posts/CreatePostModal.tsx`
  - Step 1: `MediaPicker` → Step 2: `MediaCropEditor` → Step 3: `CaptionEditor` + location text input
  - Progress indicator (MUI Stepper)
  - Submit calls `useCreatePost` hook
