import { CssBaseline, ThemeProvider } from '@mui/material';
import { BrowserRouter, Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import { Suspense, useEffect } from 'react';
import theme from './theme';
import AppShell from './AppShell';
import PostListPage from './pages/posts/PostListPage';
import PostDetailPage from './pages/posts/PostDetailPage';
import { navigationRef } from './lib/navigationRef';
import { LoginPage } from './pages/auth/LoginPage';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { PageLoader } from './components/common/PageLoader';
import { RegisterPage } from './pages/auth/RegisterPage';
import { ForgotPasswordPage } from './pages/auth/ForgotPasswordPage';
import { ProfilePage } from './pages/users/ProfllePage';
import { ProtectedRoute } from './components/common/ProtectedRoute';
import { OAuth2CallbackPage } from './pages/auth/OAuth2CallbackPage';

function GlobalNavigation() {
  const navigate = useNavigate();
  useEffect(() => {
    navigationRef.navigate = navigate;
  }, [navigate]);
  return null;
}

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        <Suspense fallback={<PageLoader />}>
          <GlobalNavigation />
          <Routes>
            <Route element={<AppShell />}>
              <Route index element={<Navigate to="/posts" replace />} />
              <Route path="/posts" element={<ErrorBoundary><PostListPage /></ErrorBoundary>} />
              <Route path="/posts/:id" element={<ErrorBoundary><PostDetailPage /></ErrorBoundary>} />
              <Route path="/login" element={<ErrorBoundary><LoginPage /></ErrorBoundary>} />
              <Route path="/register" element={<ErrorBoundary><RegisterPage /></ErrorBoundary>} />
              <Route path="/forgot-password" element={<ErrorBoundary><ForgotPasswordPage /></ErrorBoundary>} />
              {/* <Route path="/reset-password" element={<ErrorBoundary><ResetPasswordPage /></ErrorBoundary>} /> */}
              <Route path="/oauth2/callback" element={<OAuth2CallbackPage />} />

              <Route element={<ProtectedRoute />}>
                <Route path="/profile" element={<ErrorBoundary><ProfilePage /></ErrorBoundary>} />
              </Route>
            </Route>
          </Routes>
        </Suspense>
      </BrowserRouter>
    </ThemeProvider>
  );
}
