import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { PageLoader } from "../../components/common/PageLoader";

export function OAuth2CallbackPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        const accessToken = searchParams.get('accessToken');
        const refreshToken = searchParams.get('refreshToken');
        if (accessToken && refreshToken) {
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            navigate('/', { replace: true });
        } else {
            navigate('/login', { replace: true });
        }
    }, []);

    return <PageLoader />;
}