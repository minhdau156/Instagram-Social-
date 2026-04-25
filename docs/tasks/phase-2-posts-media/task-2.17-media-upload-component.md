# TASK-2.17 — Media upload component

## 📝 Overview

The goal of this task is to implement the **Media upload component** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

### UI/UX Requirements
- **Styling:** Use Material UI (MUI) components (`Stack`, `Box`, `Typography`, `Button`).
- **State Management:** Use local state (`useState`) or form state (`react-hook-form`).
- **Responsiveness:** Ensure components work on mobile and desktop by utilizing MUI's `Grid` or responsive props.
- **Error Handling:** Display clear, user-friendly error messages when API calls fail.

## 📂 File Locations

```text
frontend/src/components/posts/MediaPicker.tsx
frontend/src/components/posts/MediaCropEditor.tsx
```

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## 💡 Example

```tsx
// frontend/src/components/posts/MediaPicker.tsx
interface MediaPickerProps {
  onFilesSelected: (files: File[]) => void;
  maxFiles?: number;
}

export const MediaPicker: React.FC<MediaPickerProps> = ({
  onFilesSelected, maxFiles = 10
}) => {
  const [previews, setPreviews] = useState<string[]>([]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files ?? []).slice(0, maxFiles);
    setPreviews(files.map(f => URL.createObjectURL(f)));
    onFilesSelected(files);
  };

  return (
    <Box
      onDragOver={(e) => e.preventDefault()}
      onDrop={(e) => { e.preventDefault(); handleChange({ target: e.dataTransfer } as any); }}
      sx={{ border: '2px dashed grey', borderRadius: 2, p: 3, textAlign: 'center' }}
    >
      <Button component="label" variant="contained">
        Select Photos/Videos
        <input hidden type="file" multiple accept="image/*,video/*" onChange={handleChange} />
      </Button>
      <Stack direction="row" flexWrap="wrap" gap={1} mt={2}>
        {previews.map((src, i) => (
          <Box key={i} component="img" src={src} sx={{ width: 80, height: 80, objectFit: 'cover', borderRadius: 1 }} />
        ))}
      </Stack>
    </Box>
  );
};
```

## ✅ Checklist

- [ ] Create `frontend/src/components/posts/MediaPicker.tsx`
  - Drag & drop + file input button
  - Preview thumbnails for picked images/videos
  - Max 10 files limit
- [ ] Create `frontend/src/components/posts/MediaCropEditor.tsx`
  - Integrate `react-easy-crop` for image cropping
  - Output cropped image as `Blob`
