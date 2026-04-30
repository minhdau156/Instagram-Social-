import { createContext, useEffect, useState } from "react";
import { AuthTokens, LoginPayload, RegisterPayload } from "../types/auth";
import { UserProfile } from "../types/user";
import { authApi } from "../api/authApi";
import { usersApi } from "../api/usersApi";

interface AuthContextValue {
    user: UserProfile | null;
    tokens: AuthTokens | null;
    isLoading: boolean;
    isAuthenticated: boolean;
    login: (payload: LoginPayload) => Promise<void>;
    register: (payload: RegisterPayload) => Promise<void>;
    logout: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextValue | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [user, setUser] = useState<UserProfile | null>(null);
    const [tokens, setTokens] = useState<AuthTokens | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
            setIsLoading(false);
            return;
        }
        usersApi.getMe()
            .then(user => {
                setUser(user);
                setIsAuthenticated(true);
            })
            .catch(() => {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
            })
            .finally(() => setIsLoading(false));
    }, []);

    const login = async (payload: LoginPayload) => {
        const tokens = await authApi.login(payload);
        localStorage.setItem('accessToken', tokens.accessToken);
        localStorage.setItem('refreshToken', tokens.refreshToken);
        setTokens(tokens);
        const user = await usersApi.getMe();
        setUser(user);
        setIsAuthenticated(true);
    };

    const register = async (payload: RegisterPayload) => {
        await authApi.register(payload);
        await login({ identifier: payload.email, password: payload.password });
    };

    const logout = async () => {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
            await authApi.logout(refreshToken).catch(() => { });
        }
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setUser(null);
        setTokens(null);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ user, tokens, isLoading, isAuthenticated, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

