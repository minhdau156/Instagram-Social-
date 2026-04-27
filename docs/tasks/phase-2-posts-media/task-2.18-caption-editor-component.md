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

## 💡 Example

```tsx
// frontend/src/components/posts/CaptionEditor.tsx
const MAX_CHARS = 2200;
const HASHTAG_REGEX = /#(\w+)/g;

interface CaptionEditorProps {
  value: string;
  onChange: (value: string) => void;
}

export const CaptionEditor: React.FC<CaptionEditorProps> = ({ value, onChange }) => {
  const remaining = MAX_CHARS - value.length;

  // Highlight hashtags in a read-only overlay (positioned over the textarea)
  const highlighted = value.replace(
    HASHTAG_REGEX,
    '<strong style="color:#1976d2">#$1</strong>'
  );

  return (
    <Box sx={{ position: 'relative' }}>
      <TextField
        multiline
        rows={5}
        fullWidth
        value={value}
        onChange={(e) => onChange(e.target.value.slice(0, MAX_CHARS))}
        placeholder="Write a caption… use #hashtags and @mentions"
        inputProps={{ maxLength: MAX_CHARS }}
      />
      <Typography
        variant="caption"
        color={remaining < 50 ? 'error' : 'text.secondary'}
        sx={{ position: 'absolute', bottom: 8, right: 12 }}
      >
        {remaining}
      </Typography>
    </Box>
  );
};
```

## ✅ Checklist

- [x] Create `frontend/src/components/posts/CaptionEditor.tsx`
  - `<textarea>` with live `#hashtag` highlighting (bold blue) using regex + span injection
  - `@mention` detection with basic user autocomplete dropdown (calls `/api/v1/search?q=&type=users`)
  - Character counter (max 2200)
