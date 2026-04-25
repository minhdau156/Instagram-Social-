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

## 💡 Example

```tsx
// frontend/src/components/posts/CreatePostModal.tsx
const steps = ['Select Media', 'Edit', 'Caption & Location'];

export const CreatePostModal: React.FC<{ open: boolean; onClose: () => void }> = ({
  open, onClose
}) => {
  const [activeStep, setActiveStep] = useState(0);
  const [files, setFiles] = useState<File[]>([]);
  const [caption, setCaption] = useState('');
  const [location, setLocation] = useState('');
  const createPost = useCreatePost();

  const handleSubmit = async () => {
    if (!files[0]) return;
    await createPost.mutateAsync({ file: files[0], payload: { caption, location } });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>Create new post</DialogTitle>
      <Stepper activeStep={activeStep} sx={{ px: 3, pt: 1 }}>
        {steps.map(label => <Step key={label}><StepLabel>{label}</StepLabel></Step>)}
      </Stepper>
      <DialogContent>
        {activeStep === 0 && <MediaPicker onFilesSelected={setFiles} />}
        {activeStep === 1 && <MediaCropEditor file={files[0]} />}
        {activeStep === 2 && (
          <Stack spacing={2}>
            <CaptionEditor value={caption} onChange={setCaption} />
            <TextField label="Location" value={location} onChange={e => setLocation(e.target.value)} />
          </Stack>
        )}
      </DialogContent>
      <DialogActions>
        {activeStep > 0 && <Button onClick={() => setActiveStep(s => s - 1)}>Back</Button>}
        {activeStep < steps.length - 1
          ? <Button variant="contained" onClick={() => setActiveStep(s => s + 1)}>Next</Button>
          : <Button variant="contained" onClick={handleSubmit} loading={createPost.isPending}>Share</Button>}
      </DialogActions>
    </Dialog>
  );
};
```

## ✅ Checklist

- [ ] Create `frontend/src/components/posts/CreatePostModal.tsx`
  - Step 1: `MediaPicker` → Step 2: `MediaCropEditor` → Step 3: `CaptionEditor` + location text input
  - Progress indicator (MUI Stepper)
  - Submit calls `useCreatePost` hook
