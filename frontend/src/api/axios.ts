import axios from 'axios';

/** Shared Axios instance — base URL handled by Vite proxy (/api → http://localhost:8080). */
const axiosInstance = axios.create({
  baseURL: '/',
  headers: {
    'Content-Type': 'application/json',
  },
});

// ── Request Interceptor ──────────────────────────────────────────────────── //
axiosInstance.interceptors.request.use((config) => {
  // Temporary: inject seed user ID until JWT auth is wired
  config.headers['X-User-Id'] = '00000000-0000-0000-0000-000000000001';
  return config;
});

// ── Response Interceptor ─────────────────────────────────────────────────── //
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.detail ??
      error.response?.data?.message ??
      error.message ??
      'An unexpected error occurred';
    return Promise.reject(new Error(message));
  },
);

export default axiosInstance;
