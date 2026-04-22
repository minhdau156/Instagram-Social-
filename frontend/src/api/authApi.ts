import { AuthTokens, LoginPayload, PasswordResetConfirmPayload, PasswordResetRequestPayload, RegisterPayload } from "../types/auth";
import { api } from "./client";
import type { User } from "../types/user";

export const authApi = {
    register: (payload: RegisterPayload) =>
        api.post<{ data: User }>('/api/v1/auth/register', payload).then(r => r.data.data),

    login: (payload: LoginPayload) =>
        api.post<{ data: AuthTokens }>('/api/v1/auth/login', payload).then(r => r.data.data),

    logout: (refreshToken: string) =>
        api.post('/api/v1/auth/logout', { refreshToken }),

    refresh: (refreshToken: string) =>
        api.post<{ data: AuthTokens }>('/api/v1/auth/refresh', { refreshToken }).then(r => r.data.data),

    requestPasswordReset: (payload: PasswordResetRequestPayload) =>
        api.post('/api/v1/auth/password-reset/request', payload),

    confirmPasswordReset: (payload: PasswordResetConfirmPayload) =>
        api.post('/api/v1/auth/password-reset/confirm', payload),
}