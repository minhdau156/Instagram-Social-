# TASK-2.12 — Register exception mappings

## 📝 Overview

The goal of this task is to implement the **Register exception mappings** feature as part of Phase 2 (Posts & Media). This component is critical for allowing users to create, view, and interact with posts in the Social Media platform.

## 🛠 Implementation Details

Follow established project patterns to complete this task successfully.

## 🧪 Testing Strategy

- **Manual Testing:** Run the frontend locally (`npm run dev`) and visually verify the UI.
- **Console Errors:** Check the browser console to ensure there are no React key warnings or unhandled exceptions.

## ✅ Checklist

- [ ] Map `PostNotFoundException` → `404`
- [ ] Map `UnauthorizedPostAccessException` → `403`
- [ ] Map `MediaUploadException` → `500`
