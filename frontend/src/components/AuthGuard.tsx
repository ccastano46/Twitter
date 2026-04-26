// src/components/AuthGuard.tsx
import { useEffect } from "react";
import { useLocation } from "wouter";
import { useAuth } from "../contexts/AuthContext";

function AuthGuard({ children }: { children: React.ReactNode }) {
    const { isAuthenticated, isLoading, getToken } = useAuth();
    const [, navigate] = useLocation();

    useEffect(() => {
        if (!isLoading && isAuthenticated) {
            getToken().then(token => {
                fetch("http://localhost:8080/user/me", {
                    headers: { Authorization: `Bearer ${token}` }
                }).then(res => {
                    if (res.status === 404) {
                        navigate("/register");
                    }
                });
            });
        }
    }, [isAuthenticated, isLoading]);

    if (isLoading) return <div>Cargando...</div>;

    return <>{children}</>;
}

export default AuthGuard;