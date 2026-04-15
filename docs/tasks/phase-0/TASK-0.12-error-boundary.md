# TASK-0.12 — Error Boundary Component

## Overview

Create a React error boundary component that catches JavaScript errors thrown during rendering and displays a user-friendly fallback UI instead of crashing the entire page. This is a safety net for unexpected runtime errors that slip past TypeScript's type system.

## Requirements

- Must implement the React error boundary lifecycle (`componentDidCatch`, `getDerivedStateFromError`).
- On error: display a friendly message and a "Try again" / "Reload" button.
- Must accept `children` as a prop so it can wrap any subtree.
- Should optionally accept a custom `fallback` prop for context-specific error UI.

## Notes

- Error boundaries **must** be class components in React — there is no hook-based equivalent. This is the one exception to the "functional components only" rule in `coding-standard.md`.
- Consider wrapping individual page-level routes with `<ErrorBoundary>` rather than wrapping the entire app, so one broken page doesn't affect navigation.
- Log caught errors to the console (or a future error tracking service like Sentry) inside `componentDidCatch`.
- The "Try again" button should call `this.setState({ hasError: false })` to allow the component tree to re-render. A "Reload page" link using `window.location.reload()` can also be provided.
- Style the fallback UI using MUI components (`Box`, `Typography`, `Button`) and the existing theme.

## Checklist

- [x] Create `frontend/src/components/common/ErrorBoundary.tsx`
  ```tsx
  interface Props {
    children: ReactNode;
    fallback?: ReactNode;
  }

  interface State {
    hasError: boolean;
    error: Error | null;
  }

  class ErrorBoundary extends Component<Props, State> {
    static getDerivedStateFromError(error: Error): State { ... }
    componentDidCatch(error: Error, info: ErrorInfo) { console.error(error, info); }
    render() { ... }
  }
  ```
- [x] Implement `getDerivedStateFromError` — return `{ hasError: true, error }`
- [x] Implement `componentDidCatch` — `console.error` the error and info (placeholder for Sentry)
- [x] Implement `render()`:
  - If `hasError` and custom `fallback` provided: render `fallback`
  - If `hasError` and no `fallback`: render default fallback UI (MUI `Box` centered, error icon, message, "Try again" `Button`)
  - Otherwise: render `children`
- [x] Export `ErrorBoundary` as a named export
- [x] Wrap each route-level page in `App.tsx` with `<ErrorBoundary>` (or add to the router layout)
- [ ] Manually verify by temporarily throwing inside a component renders the fallback instead of a white screen
