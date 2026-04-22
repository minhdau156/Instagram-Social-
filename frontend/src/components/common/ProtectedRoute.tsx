import { useAuth } from "../../hooks/useAuth";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { PageLoader } from "./PageLoader";

export function ProtectedRoute() {
    const { isAuthenticated, isLoading } = useAuth();
    const location = useLocation();
    if (isLoading) return <PageLoader />;
    if (!isAuthenticated) return <Navigate to="/login" state={{ from: location }} replace />;
    return <Outlet />;
}