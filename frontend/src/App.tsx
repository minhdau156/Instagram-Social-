import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { BrowserRouter, Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import theme from './theme';
import AppShell from './AppShell';
import PostListPage   from './pages/posts/PostListPage';
import PostDetailPage from './pages/posts/PostDetailPage';
import { navigationRef } from './lib/navigationRef';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

function GlobalNavigation() {
  const navigate = useNavigate();
  useEffect(() => {
    navigationRef.navigate = navigate;
  }, [navigate]);
  return null;
}

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <BrowserRouter>
          <GlobalNavigation />
          <Routes>
            <Route element={<AppShell />}>
              <Route index element={<Navigate to="/posts" replace />} />
              <Route path="/posts"     element={<PostListPage />} />
              <Route path="/posts/:id" element={<PostDetailPage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </ThemeProvider>
    </QueryClientProvider>
  );
}
