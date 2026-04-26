import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { useAuth0 } from "@auth0/auth0-react";

const API_URL = "http://localhost:8080";

export interface User {
    auth0Id: string;
    email: string;
    username: string;
}

interface AuthContextType {
    user: User | null;
    isLoading: boolean;
    isAuthenticated: boolean;
    login: () => void;
    logout: () => void;
    register: (username: string, email: string, password: string) => Promise<User>;
    getToken: () => Promise<string>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const {
        isLoading: auth0IsLoading,
        isAuthenticated,
        loginWithRedirect,
        logout: auth0Logout,
        getAccessTokenSilently,
    } = useAuth0();

    const [isLoading, setIsLoading] = useState(true);
    const [dbUser, setDbUser] = useState<User | null>(null);

    useEffect(() => {
        if (!auth0IsLoading && isAuthenticated) {
            getAccessTokenSilently()
                .then(token =>
                    fetch(`${API_URL}/user/me`, {
                        headers: { Authorization: `Bearer ${token}` }
                    })
                )
                .then(res => {
                    if (res.ok) return res.json();
                    return null;
                })
                .then(data => setDbUser(data))
                .finally(() => setIsLoading(false));
        } else if (!auth0IsLoading) {
            setIsLoading(false);
        }
    }, [auth0IsLoading, isAuthenticated]);

    const getToken = async () => {
        return await getAccessTokenSilently();
    };

    const login = () => {
        loginWithRedirect();
    };

    const logout = () => {
        setDbUser(null);
        auth0Logout({ logoutParams: { returnTo: window.location.origin } });
    };

    const register = async (username: string, email: string, password: string) => {
        const response = await fetch(`${API_URL}/user/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, email, password }),
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || "Registration failed");
        }

        const registeredUser: User = await response.json();
        setDbUser(registeredUser);
        return registeredUser;
    };

    const value: AuthContextType = {
        user: dbUser,
        isLoading,
        isAuthenticated,
        login,
        logout,
        register,
        getToken,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}