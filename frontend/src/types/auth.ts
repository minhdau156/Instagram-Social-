export interface AuthTokens {
    accessToken: string;
    refreshToken: string;
    expiresIn: number;
}

export interface LoginPayload {
    identifier: string;
    password: string;
}

export interface RegisterPayload {
    username: string;
    email: string;
    password: string;
    fullName: string;
}

export interface PasswordResetRequestPayload {
    email: string;
}

export interface PasswordResetConfirmPayload {
    token: string;
    newPassword: string;
}